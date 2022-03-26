package com.fongloo.base.status;

import com.alibaba.fastjson.JSONObject;
import com.fongloo.base.exception.BizException;
import com.fongloo.base.exception.code.BaseExceptionCode;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 状态类
 */
public class R<T> {
    public static final String DEF_ERROR_MESSAGE = "系统繁忙，请稍后再试";
    public static final String HYSTRIX_ERROR_MESSAGE = "请求超时，请稍后再试";
    public static final int SUCCESS_CODE = 0;
    public static final int FAIL_CODE = -1;
    public static final int TIMEOUT_CODE = -2;

    /**
     * 统一参数验证异常
     */
    public static final int VALID_EX_CODE = -9;
    public static final int OPERATION_EX_CODE = -10;

    /**
     * 调用是否成功标识 0：成功，-1:系统繁忙，此时请开发者稍候再试 详情见[ExceptionCode]
     */
    @ApiModelProperty("响应编码: 0/200 - 请求处理成功")
    private int code;

    /**
     * 调用结果
     */
    @ApiModelProperty("响应数据")
    private T date;

    /**
     * 结果消息，如果调用成功，消息通常为空
     */
    @ApiModelProperty("提示消息")
    private String message = "ok";

    /**
     * 请求路径
     */
    @ApiModelProperty("请求路径")
    private String path;

    /**
     * 附加数据
     */
    @ApiModelProperty("附加数据")
    private Map<String, Object> extra;

    /**
     * 响应时间
     */
    @ApiModelProperty("响应时间戳")
    private long timestamp = System.currentTimeMillis();


    public R() {
        super();
    }

    public R(int code, T date, String message) {
        this.code = code;
        this.date = date;
        this.message = message;
    }

    public static <E> R<E> result(int code, E data, String message) {
        return new R<>(code, data, message);
    }

    /**
     * 请求成功
     *
     * @return RPC调用结果 ==> true
     */
    public static R<Boolean> success() {
        return new R<>(SUCCESS_CODE, true, "ok");
    }

    /**
     * 请求成功
     *
     * @param data 结果
     * @return RPC调用结果
     */
    public static <E> R<E> success(E data) {
        return new R<>(SUCCESS_CODE, data, "ok");
    }

    /**
     * 请求成功 ，data返回值，msg提示信息
     *
     * @param data    结果
     * @param message 消息
     * @return RPC调用结果
     */
    public static <E> R<E> success(E data, String message) {
        return new R<>(SUCCESS_CODE, data, message);
    }

    /**
     * 请求失败消息
     *
     * @param code    错误码
     * @param message 如果为NULL或"",则 = 系统繁忙，请稍后再试
     * @return RPC调用结果
     */
    public static <E> R<E> fail(int code, String message) {
        return new R<>(code, null, (message == null || message.isEmpty()) ? DEF_ERROR_MESSAGE : message);
    }

    public static <E> R<E> fail(String message) {
        return fail(OPERATION_EX_CODE, message);
    }

    public static <E> R<E> fail(String message, Object... args) {
        String msg = (message == null || message.isEmpty()) ? DEF_ERROR_MESSAGE : message;
        return new R<>(OPERATION_EX_CODE, null, String.format(message, args));
    }

    public static <E> R<E> fail(BaseExceptionCode exceptionCode) {
        return validFail(exceptionCode);
    }

    public static <E> R<E> fail(BizException exception) {
        if (exception == null) {
            return fail(DEF_ERROR_MESSAGE);
        }
        return new R<>(exception.getCode(), null, exception.getMessage());
    }

    /**
     * 请求失败消息，根据异常类型，获取不同的提供消息
     *
     * @param throwable 异常
     * @return RPC调用结果
     */
    public static <E> R<E> fail(Throwable throwable) {
        return fail(FAIL_CODE, throwable != null ? throwable.getMessage() : DEF_ERROR_MESSAGE);
    }

    public static <E> R<E> validFail(String message) {
        return new R<>(VALID_EX_CODE, null, (message == null || message.isEmpty()) ? DEF_ERROR_MESSAGE : message);
    }

    public static <E> R<E> validFail(String message, Object... args) {
        String msg = (message == null || message.isEmpty()) ? DEF_ERROR_MESSAGE : message;
        return new R<>(VALID_EX_CODE, null, String.format(msg, args));
    }

    public static <E> R<E> validFail(BaseExceptionCode exceptionCode) {
        String msg = (exceptionCode.getMessage() == null || exceptionCode.getMessage().isEmpty()) ? DEF_ERROR_MESSAGE : success().message;
        return new R<>(VALID_EX_CODE, null, msg);
    }

    /**
     * 超时
     *
     * @param <E>
     * @return
     */
    public static <E> R<E> timeout() {
        return fail(TIMEOUT_CODE, HYSTRIX_ERROR_MESSAGE);
    }

    /**
     * 额外数据
     *
     * @param key   key
     * @param value 额外数据
     * @return
     */
    public R<T> put(String key, Object value) {
        if (this.extra == null) {
            this.extra = new HashMap<>();
        }
        this.extra.put(key, value);
        return this;
    }

    /**
     * 逻辑是否处理成功
     */
    public Boolean getIsSuccess() {
        return this.code == SUCCESS_CODE || this.code == 200;
    }

    /**
     * 逻辑处理是否失败
     */
    public Boolean getIsError() {
        return !getIsSuccess();
    }

    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
