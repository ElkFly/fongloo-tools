package com.fongloo.user.interceptor;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Slf4j
public class ContextHandlerInterceptor extends HandlerInterceptorAdapter {
    // TODO 应该需要一个公共的TheadLocal
    public static final ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<>();

    /**
     * 网关：
     * 获取token，并解析，然后将所有的用户、应用信息封装到请求头
     * <p>
     * 拦截器：
     * 解析请求头数据， 将用户信息、应用信息封装到TheadLocal
     * 考虑请求来源是否网关（ip等）
     * <p>
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            if (!(handler instanceof HandlerMethod)) {
                log.info("不能执行: url = {}", request.getRequestURI());
                return super.preHandle(request, response, handler);
            }
            // 从网关解析token得到用户信息
            // TODO 此处应该为封装为常量
            String userId = getHeader(request, "userid");
            String account = getHeader(request, "account");
            String name = getHeader(request, "name");
            String orgId = getHeader(request, "orgid");
            String stationId = getHeader(request, "stationid");

            Map<String, String> map = threadLocal.get();
            // 把用户信息存到本地线程(ThreadLocal)
            // TODO 此处应该封装一个公共存放的ThreadLocal
            map.put("userid", userId);
            map.put("account", account);
            map.put("name", name);
            map.put("orgid", orgId);
            map.put("stationid", stationId);

            threadLocal.set(map);

        } catch (Exception ex) {
            log.warn("解析Token信息时，发生异常，原因: {}", ex);
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * 获取指定请求头 参数值
     * @param request  请求对象
     * @param name     参数名
     * @return
     */
    private String getHeader(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        if (StrUtil.isEmpty(value)) {
            return null;
        }
        try {
            // URL解码
            // TODO 解码编码应该是一个工具类
            return URLDecoder.decode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 拦截结束
     * 清空ThreaLocal
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // TODO 清空ThreadLocal
        threadLocal.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
