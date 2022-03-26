package com.fongloo.base.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基础控制器
 */
public abstract class BaseController {
    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

    /**
     * 当前页
     */
    protected static final String CURRENT = "current";

    /**
     * 每页显示条数
     */
    protected static final String SIZE = "size";

    /**
     * 排序字段 ASC
     */
    protected static final  String PAGE_ASCS = "ascs";

    /**
     * 排序字段
     */
    protected static final  String PAGE_DESCS = "descs";

    /**
     * 开始时间
     */
    protected static final String START_CREATE_TIME = "startCreateTime";

    /**
     * 结束时间
     */
    protected static final String END_CREATE_TIME = "endCreateTime";

    /**
     * 默认每页条目20，最大条数100
     */
    int DEFAULT_LIMIT = 20;
    int MAX_LIMIT = 100;

    /**
     * 成功返回
     */

}
