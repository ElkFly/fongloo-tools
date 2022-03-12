package com.fongloo.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fongloo.wrapper.XssRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 过滤配置
 * 过滤入口 doFile
 */
@Slf4j
public class XssFilter implements Filter {
    /**
     * 可放行的请求路径
     * 可以使用逗号分隔
     */
    private static final String IGNORE_PATH = "ignorePath";

    /**
     * 可放行的请求路径列表
     */
    private static List<String> ignorePathList = null;

    /**
     * 可放行的参数值
     * 可以使用逗号分隔
     */
    private static final String IGNORE_PARAM_VALUE = "ignoreParamValue";

    /**
     * 可放行的参数值列表
     */
    private List<String> ignoreParamValueList = null;

    /**
     * 默认放行单点登录的登出响应
     * (响应中包含samlp:LogoutRequest标签，直接放行)
     */
    private static final String CAS_LOGOUT_RESPONSE_TAG = "samlp:LogoutRequest";


    /**
     * 初始化配置
     */
    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("开始初始化...");

        String ignorePaths = filterConfig.getInitParameter(IGNORE_PATH);
        String ignoreParamValues = filterConfig.getInitParameter(IGNORE_PARAM_VALUE);

        if (!StrUtil.isBlank(ignorePaths)) {
            // 有逗号 => 可放行的请求路径列表
            String[] ignorePathArr = ignorePaths.split(",");
            ignorePathList = Arrays.asList(ignorePathArr);
        }

        if (StrUtil.isBlank(ignoreParamValues)) {
            //默认放行单点登录的登出响应(响应中包含samlp:LogoutRequest标签，直接放行)
            ignoreParamValueList = new ArrayList<String>();
            ignoreParamValueList.add(CAS_LOGOUT_RESPONSE_TAG);

        } else {

            // 有逗号 => 可放行的请求参数列表
            String[] ignoreParamValueArr = ignoreParamValues.split(",");
            ignoreParamValueList = Arrays.asList(ignoreParamValueArr);

            //默认放行单点登录的登出响应(响应中包含samlp:LogoutRequest标签，直接放行)
            if (!ignoreParamValueList.contains(CAS_LOGOUT_RESPONSE_TAG)) {
                ignoreParamValueList.add(CAS_LOGOUT_RESPONSE_TAG);
            }
        }

        log.debug("可放行的请求路径列表 : {}", JSONUtil.toJsonStr(ignorePathList));
        log.debug("可放行的参数值列表 : {}", JSONUtil.toJsonStr(ignoreParamValueList));
        log.debug("初始化完毕......");
    }

    /**
     * 判断是否可以忽略的路径
     * 判断完成后进入Wrapper类进行过滤
     *
     * @param request     请求
     * @param response    响应
     * @param filterChain 过滤相关类
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.debug("开始过滤");
        String urlPath = ((HttpServletRequest) request).getRequestURI();
        // 判断URL是否可以忽略过滤
        if (isIgnorePath(urlPath)) {
            log.debug("忽略此请求路径:[ {} ]", urlPath);
            filterChain.doFilter(request, response);
            return;
        } else {
            log.debug("请求路径:[ {} ] 开始过滤 ==> XssRequestWrapper类", urlPath);
            filterChain.doFilter(new XssRequestWrapper((HttpServletRequest) request, ignorePathList), response);
        }
        log.debug("过滤结束");
    }


    /**
     * 判断是否为可忽略路径
     * <p>
     * 可忽略：servletPath为空
     * 可放行的路径里面包含servletPath
     * <p>
     * 不可忽略：servletPath不为空
     * 或可放行的路径里面不包含servletPath
     *
     * @param servletPath
     * @return
     */
    private static boolean isIgnorePath(String servletPath) {
        if (StrUtil.isBlank(servletPath)) {
            return true;
        }
        if (CollectionUtil.isEmpty(ignorePathList)) {
            return false;
        } else {
            for (String ignorePath : ignorePathList) {
                if (!StrUtil.isBlank(ignorePath) && servletPath.contains(ignorePath.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        log.debug("过滤器已销毁");
    }

}
