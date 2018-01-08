package com.raythonsoft.auth.page;

import com.github.pagehelper.Page;
import com.github.pagehelper.dialect.MySqlDialect;
import com.github.pagehelper.util.SqlUtil;
import com.raythonsoft.auth.common.ProjectConstant;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.util.Map;

/**
 * Created by Anur IjuoKaruKas on 2017/9/28.
 * Description : 自定义方言
 */
public class CustomDialect extends MySqlDialect {

    public CustomDialect(SqlUtil sqlUtil) {
        super(sqlUtil);
    }

    @Override
    public Object processPageParameter(MappedStatement ms, Map<String, Object> paramMap, Page page, BoundSql boundSql, CacheKey pageKey) {
        return super.processPageParameter(ms, paramMap, page, boundSql, pageKey);
    }

    @Override
    public String getPageSql(String sql, Page page, RowBounds rowBounds, CacheKey pageKey) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
        sqlBuilder.append(sql);
        if (sql.contains(ProjectConstant.SQL_SIGN)) {// 如果需要特殊分页

//            StringBuilder sb = new StringBuilder(sql);
//            sb.indexOf(ProjectConstant.SQL_SIGN);

            StringBuilder mae = new StringBuilder(sqlBuilder.substring(0, sqlBuilder.indexOf(ProjectConstant.SQL_SIGN)));// mae 截止sql语句到 limitable
            StringBuilder uShiRo = new StringBuilder(sqlBuilder.substring(sqlBuilder.indexOf(ProjectConstant.SQL_SIGN), sqlBuilder.length()));// 剩余的

            mae.insert(mae.lastIndexOf(")"), " limit ?,?");
            return mae.append(uShiRo).toString();
        }
        sqlBuilder.append(" limit ?,?");
        return sqlBuilder.toString();
    }
}
