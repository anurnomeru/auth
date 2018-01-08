package com.raythonsoft.auth.common;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    public static Result genSuccessResult() {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setStatus("SUCCESS");
    }

    public static Result genSuccessResult(Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setData(data)
                .setStatus("SUCCESS");
    }

    public static Result genFailResult(String message, String status) {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(message)
                .setStatus(status);
    }

    public static Result genExceptionResult(Exception e) {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(e.getMessage())
                .setStatus(e.getClass().getSimpleName());
    }
}
