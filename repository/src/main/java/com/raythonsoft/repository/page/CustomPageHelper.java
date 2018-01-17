package com.raythonsoft.repository.page;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * Created by Anur IjuoKaruKas on 2017/9/28.
 * Description : 自定义拦截器
 */
@SuppressWarnings("rawtypes")
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class CustomPageHelper extends PageHelper {
    private final CustomSqlUtil sqlUtil = new CustomSqlUtil();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return sqlUtil.intercept(invocation);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        sqlUtil.setProperties(properties);
    }
}
