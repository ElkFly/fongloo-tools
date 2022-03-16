package com.fongloo.log.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.fongloo.log.entity.OptLogDTO;
import com.fongloo.log.utils.LogUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 操作日志使用spring event异步入库
 */
@Slf4j
@Aspect
public class SysLogAspect {
    private static final ThreadLocal<OptLogDTO> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 拦截带有@SysLog注解的方法
     */
    @Pointcut("@annotation(com.fongloo.log.annotation.SysLog)")
    public void sysLogAspect() {
    }

    ;

    /**
     * 获取日志记录需要的属性
     *
     * @return 记录日志实体类
     */
    private OptLogDTO getOptLogDTO() {
        OptLogDTO optLogDTO = THREAD_LOCAL.get();
        if (optLogDTO == null) {
            return new OptLogDTO();
        }
        return optLogDTO;
    }


    /**
     * 前置切面
     *
     * @param joinPoint
     */
    @Before("sysLogAspect()")
    public void recordLog(JoinPoint joinPoint) {
        // 设置操作人
        OptLogDTO optLogDTO = getOptLogDTO();

//        optLogDTO.setCreateUser();
//        optLogDTO.setUserName();


        /**
         * 如果有swagger的配置信息
         * 按照@Api.tag 控制器的描述信息
         */
        String controllerDescription = "";
        Api api = joinPoint.getTarget().getClass().getAnnotation(Api.class);
        if (api != null) {
            String[] tags = api.tags();
            if (tags != null || tags.length > 0) {
                controllerDescription = tags[0];
            }
        }

        // 获取到带有@SysLog方法的参数信息
        String controllerMethodDescription = LogUtils.getControllerMethodDescription(joinPoint);

        // 实体类填充
        if (StrUtil.isEmpty(controllerDescription)) {
            optLogDTO.setDescription(controllerDescription);
        } else {
            optLogDTO.setDescription(controllerDescription + " - " + controllerMethodDescription);
        }

        // 类名
        optLogDTO.setClassPath(joinPoint.getTarget().getClass().getName());
        // 请求方法
        optLogDTO.setRequestMethod(joinPoint.getSignature().getName());

        // 参数
        Object[] args = joinPoint.getArgs();
        String strArgs = "";

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 将请求内容封装为JSON
        if (!request.getContentType().contains("multipart/form-data")) {
            strArgs = JSONUtil.toJsonStr(args);
        }

        // 请求参数(内容)
        optLogDTO.setRequestParams(getText(strArgs));

        if (request != null) {
            // ip
            optLogDTO.setRequestIp(ServletUtil.getClientIP(request));
            // 请求URL
            optLogDTO.setRequestUrl(URLUtil.getPath(request.getRequestURI()));
            // 请求类型(get/post...)
            optLogDTO.setRequestType(request.getMethod());
            // 获取请求头中的 HTTP_USER_AGENT
            optLogDTO.setUserAgent(StrUtil.sub(request.getHeader("user-agent"), 0, 500));
        }
        // 开始时间
        optLogDTO.setStartTime(LocalDateTime.now());

        // 将日志实体保存到本地线程
        THREAD_LOCAL.set(optLogDTO);
    }

    /**
     * 截取过长的文本
     *
     * @param text 需要截取的文本
     * @return
     */
    private String getText(String text) {
        return StrUtil.sub(text, 0, 88888);
    }
}
