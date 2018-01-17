package com.raythonsoft.repository.common.model;

import com.github.pagehelper.util.StringUtil;
import lombok.Data;

import javax.persistence.Transient;


/**
 * Created by Anur IjuoKaruKas on 2017/10/6.
 * Description : 排列传输对象
 */
@Data
public class SortModel {
    @Transient
    private String orderBy; // 排列字段、

    @Transient
    private String sort; // asc desc 等。

    public String orderSql() {
        StringBuilder order = new StringBuilder();

        if (StringUtil.isNotEmpty(orderBy)) {
            order.append(orderBy);
        } else {
            return null;
        }

        if (StringUtil.isNotEmpty(sort)) {
            order.append(" ");
            order.append(sort);
        } else {
            return null;
        }

        return order.toString();
    }
}
