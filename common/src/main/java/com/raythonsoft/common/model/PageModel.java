package com.raythonsoft.common.model;

import lombok.Data;



/**
 * Created by Anur IjuoKaruKas on 2017/12/15.
 * Description : 基础分页传输对象
 */
@Data
public class PageModel {

    Integer pageNum = 1;// 默认第一页

    Integer pageSize = 10;// 十条

    SortModel sortModel = new SortModel();

    /**
     * 获取排列sql语句，如果没传入则为空，缺少字段也为空
     *
     * @return
     */
    public String orderSql() {
        return sortModel.orderSql();
    }

    /**
     * 根据分页计算，从哪条数据开始偏移
     *
     * @return
     */
    public Integer dataStart() {
        if (pageNum > 0) {
            return pageNum = (pageNum - 1) * dataCount();
        }
        return 0;
    }

    /**
     * 获取偏移量
     *
     * @return
     */
    public Integer dataCount() {
        if (pageSize < 0) {
            return 0;
        }
        return pageSize;
    }
}
