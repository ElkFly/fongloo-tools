package com.fongloo.log.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 系统日志记录属性
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true) //链式编程
@ToString(callSuper = true) //并且打印父类的属性信息
public class OptLogDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 操作IP
     */
    private String requestIp;

    /**
     * 日志类型
     * [OPT: 操作类型; EX: 异常类型]
     */
    private String type;

    /**
     * 操作人
     */
    private String userName;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 类路径
     */
    private String classPath;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求类型
     * (GET/POST...)
     */
    private String requestType;

    /**
     * 请求参数（内容）
     */
    private String requestParams;

    /**
     * 返回值
     */
    private String result;

    /**
     * 异常详细信息
     */
    private String exDetail;

    /**
     * 异常描述
     */
    private String exDesc;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 消耗时间
     */
    private Long consumingTime;

    /**
     * 浏览器
     */
    private String userAgent;


    private Long createUser;
}

