package com.raythonsoft.auth.page;

import com.github.pagehelper.Dialect;
import com.github.pagehelper.util.MSUtils;
import com.github.pagehelper.util.SqlUtil;
import com.github.pagehelper.util.StringUtil;
import com.raythonsoft.auth.common.ProjectConstant;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Anur IjuoKaruKas on 2017/9/28.
 * Description : 自定义sql处理工具
 */
public class CustomSqlUtil extends SqlUtil {
    private Dialect dialect;
    private Field additionalParametersField;
    private Properties properties;

    protected boolean autoDialect = true;    //自动获取dialect,如果没有setProperties或setSqlUtilConfig，也可以正常进行
    protected boolean autoRuntimeDialect;    //运行时自动获取dialect
    protected boolean closeConn = true;    //多数据源时，获取jdbcurl后是否关闭数据源
    private Map<String, Dialect> urlDialectMap = new ConcurrentHashMap<String, Dialect>();    //缓存
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 真正的拦截器方法
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            return doIntercept(invocation);
        } finally {
            clearLocalPage();
        }
    }

    /**
     * 真正的拦截器方法
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object doIntercept(Invocation invocation) throws Throwable {
        //获取拦截方法的参数
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        List resultList;
        if (autoDialect) {
            lock.lock();
            try {
                if (autoDialect) {
                    autoDialect = false;
                    this.dialect = getDialect(ms);
                }
            } finally {
                lock.unlock();
            }
        }
        Dialect runtimeDialect = dialect;
        if (autoRuntimeDialect) {
            runtimeDialect = getDialect(ms);
        }
        //调用方法判断是否需要进行分页，如果不需要，直接返回结果
        if (!runtimeDialect.skip(ms, parameterObject, rowBounds)) {
            ResultHandler resultHandler = (ResultHandler) args[3];
            //当前的目标对象
            Executor executor = (Executor) invocation.getTarget();
            BoundSql boundSql = ms.getBoundSql(parameterObject);
            //反射获取动态参数
            Map<String, Object> additionalParameters = (Map<String, Object>) additionalParametersField.get(boundSql);
            //判断是否需要进行 count 查询
            if (runtimeDialect.beforeCount(ms, parameterObject, rowBounds)) {
                //创建 count 查询的缓存 key
                CacheKey countKey = executor.createCacheKey(ms, parameterObject, RowBounds.DEFAULT, boundSql);
                countKey.update("_Count");
                MappedStatement countMs = msCountMap.get(countKey);
                if (countMs == null) {
                    //根据当前的 ms 创建一个返回值为 Long 类型的 ms
                    countMs = MSUtils.newCountMappedStatement(ms);
                    msCountMap.put(countKey, countMs);
                }
                //调用方言获取 count sql
                String countSql = runtimeDialect.getCountSql(ms, boundSql, parameterObject, rowBounds, countKey);

                List<ParameterMapping> countTempParameterMappingList = new ArrayList<>();

                //**************** 截取有效部分进行count操作 by Anur 17/10/25
                if (countSql.contains(ProjectConstant.SQL_SIGN)) {
                    Integer subIndex = countSql.indexOf(ProjectConstant.SQL_SIGN) + ProjectConstant.SQL_SIGN.length();// 截取位置 到AS limitable
                    countSql = countSql.substring(0, subIndex);// 进行截取

                    Integer usefulParamCount = 0;// 参数舍弃
                    for (int i = 0; i < countSql.length(); i++) {
                        if (countSql.indexOf("?", i) != -1) {
                            usefulParamCount++;
                            i = countSql.indexOf("?", i);
                        }
                    }
                    List<ParameterMapping> allCountTempParameterMappingList = boundSql.getParameterMappings();
                    countTempParameterMappingList = new ArrayList<>();

                    for (Iterator<ParameterMapping> iterator = allCountTempParameterMappingList.iterator(); iterator.hasNext(); ) {
                        if (usefulParamCount == 0) {
                            break;
                        }
                        usefulParamCount--;
                        ParameterMapping next = iterator.next();
                        countTempParameterMappingList.add(next);
                    }
                } else {
                    countTempParameterMappingList = boundSql.getParameterMappings();
                }

                BoundSql countBoundSql = new BoundSql(ms.getConfiguration(), countSql, countTempParameterMappingList, parameterObject);
                //当使用动态 SQL 时，可能会产生临时的参数，这些参数需要手动设置到新的 BoundSql 中
                for (String key : additionalParameters.keySet()) {
                    countBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
                }

                Object countResultList = executor.query(countMs, parameterObject, RowBounds.DEFAULT, resultHandler, countKey, countBoundSql);// 执行 count 查询
                Long count = (Long) ((List) countResultList).get(0);// 处理查询总数
                runtimeDialect.afterCount(count, parameterObject, rowBounds);
                if (count == 0L) { // 当查询总数为 0 时，直接返回空的结果
                    return runtimeDialect.afterPage(new ArrayList(), parameterObject, rowBounds);
                }
            }

            List<ParameterMapping> pageTempParameterMappingList;
            // 判断是否需要进行分页查询
            if (runtimeDialect.beforePage(ms, parameterObject, rowBounds)) {
                CacheKey pageKey = executor.createCacheKey(ms, parameterObject, rowBounds, boundSql);// 生成分页的缓存 key
                parameterObject = runtimeDialect.processParameterObject(ms, parameterObject, boundSql, pageKey);// 处理参数对象
                String pageSql = runtimeDialect.getPageSql(ms, boundSql, parameterObject, rowBounds, pageKey);// 调用方言获取分页 sql

                //**************** 参数调度 by Anur 17/10/25
                if (pageSql.contains(ProjectConstant.SQL_SIGN)) {
                    String sqlInCount = pageSql.substring(0, pageSql.indexOf(ProjectConstant.SQL_SIGN));
                    Integer count = -2;
                    for (int i = 0; i < sqlInCount.length(); i++) {
                        if (sqlInCount.indexOf("?", i) != -1) {
                            i = sqlInCount.indexOf("?", i);
                            count++;
                        }
                    }
                    List<ParameterMapping> tempParameterMappingList = boundSql.getParameterMappings();
                    ParameterMapping parameterFirst = tempParameterMappingList.get(tempParameterMappingList.size() - 2);
                    ParameterMapping parameterSecond = tempParameterMappingList.get(tempParameterMappingList.size() - 1);
                    tempParameterMappingList.add(0 + count, parameterFirst);
                    tempParameterMappingList.add(1 + count, parameterSecond);
                    tempParameterMappingList.remove(tempParameterMappingList.size() - 2);
                    tempParameterMappingList.remove(tempParameterMappingList.size() - 1);

                    pageTempParameterMappingList = tempParameterMappingList;
                } else {
                    pageTempParameterMappingList = boundSql.getParameterMappings();
                }
                //****************

                BoundSql pageBoundSql = new BoundSql(ms.getConfiguration(), pageSql, pageTempParameterMappingList, parameterObject);
                //设置动态参数
                for (String key : additionalParameters.keySet()) {
                    pageBoundSql.setAdditionalParameter(key, additionalParameters.get(key));
                }
                //执行分页查询
                resultList = executor.query(ms, parameterObject, RowBounds.DEFAULT, resultHandler, pageKey, pageBoundSql);
            } else {
                resultList = new ArrayList();
            }
        } else {
            args[2] = RowBounds.DEFAULT;
            resultList = (List) invocation.proceed();
        }
        //返回默认查询
        return runtimeDialect.afterPage(resultList, parameterObject, rowBounds);
    }

    /**
     * 初始化 dialect
     *
     * @param dialectClass
     * @param properties
     */
    private Dialect initDialect(String dialectClass, Properties properties) {
        Dialect dialect;
        try {
            dialect = new CustomDialect(this);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("初始化 dialect [" + dialectClass + "]时出错:" + e.getMessage());
        }
        dialect.setProperties(properties);
        return dialect;
    }

    /**
     * 获取url
     *
     * @param dataSource
     * @return
     */
    @Override
    public String getUrl(DataSource dataSource) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return conn.getMetaData().getURL();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    if (closeConn) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    //ignore
                }
            }
        }
    }

    /**
     * 根据datasource创建对应的sqlUtil
     *
     * @param ms
     */
    @Override
    public Dialect getDialect(MappedStatement ms) {
        //改为对dataSource做缓存
        DataSource dataSource = ms.getConfiguration().getEnvironment().getDataSource();
        String url = getUrl(dataSource);
        if (urlDialectMap.containsKey(url)) {
            return urlDialectMap.get(url);
        }
        try {
            lock.lock();
            if (urlDialectMap.containsKey(url)) {
                return urlDialectMap.get(url);
            }
            if (StringUtil.isEmpty(url)) {
                throw new RuntimeException("无法自动获取jdbcUrl，请在分页插件中配置dialect参数!");
            }
            String dialectStr = fromJdbcUrl(url);
            if (dialectStr == null) {
                throw new RuntimeException("无法自动获取数据库类型，请通过 dialect 参数指定!");
            }
            Dialect dialect = initDialect(dialectStr, properties);
            urlDialectMap.put(url, dialect);
            return dialect;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        //多数据源时，获取jdbcurl后是否关闭数据源
        String closeConn = properties.getProperty("closeConn");
        //解决#97
        if (StringUtil.isNotEmpty(closeConn)) {
            this.closeConn = Boolean.parseBoolean(closeConn);
        }
        //数据库方言
        String dialect = properties.getProperty("dialect");
        String runtimeDialect = properties.getProperty("autoRuntimeDialect");
        if (StringUtil.isNotEmpty(runtimeDialect) && runtimeDialect.equalsIgnoreCase("TRUE")) {
            this.autoRuntimeDialect = true;
            this.autoDialect = false;
            this.properties = properties;
        } else if (StringUtil.isEmpty(dialect)) {
            autoDialect = true;
            this.properties = properties;
        } else {
            autoDialect = false;
            this.dialect = initDialect(dialect, properties);
        }
        try {
            //反射获取 BoundSql 中的 additionalParameters 属性
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
