package com.raythonsoft.common.util;


import com.raythonsoft.common.model.Result;
import com.raythonsoft.common.model.ResultCode;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    public static Result genSuccessResult() {
        return new Result()
                .setCode(ResultCode.SUCCESS);
    }

    public static Result genSuccessResult(Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setData(data);
    }

    public static Result genFailResult(String message) {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(message);
    }

    public static Result genExceptionResult(Exception e) {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(e.getMessage());
    }
}
