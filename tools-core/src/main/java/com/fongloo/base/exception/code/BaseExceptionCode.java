package com.fongloo.base.exception.code;

/**
 * 异常接口类
 * 用于全局错误码
 */
public interface BaseExceptionCode {
    /**
     * 异常编码
     */
    int getCode();

    /**
     * 异常消息
     */
    String getMessage();
}
