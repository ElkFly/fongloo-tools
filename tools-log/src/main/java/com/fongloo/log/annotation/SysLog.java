package com.fongloo.log.annotation;

import java.lang.annotation.*;

/**
 *  用于方法上
 *  此注解用于请求参数与响应的日志记录
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    /**
     * 描述
     * @return {String}
     */
    String value();

    /**
     * 记录请求参数
     * @return {boolean}
     */
    boolean recordRequestParm() default true;

    /**
     * 记录响应参数
     * @return
     */
    boolean recordResponseParm() default true;
}
