package com.fongloo.log.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.fongloo.log.entity.OptLogDTO;
import com.fongloo.log.event.SysLogEvent;
import com.fongloo.log.utils.LogUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 操作日志使用spring event异步入库
 */
@Slf4j
@Aspect
public class SysLogAspect {
    private static final ThreadLocal<OptLogDTO> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 用于发布事件
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 拦截带有@SysLog注解的方法
     */
    @Pointcut("@annotation(com.fongloo.log.annotation.SysLog)")
    public void sysLogAspect() {
    }

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
        tryCatch((aaa) -> {

            // TODO 设置操作人
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
            try {
                if (!request.getContentType().contains("multipart/form-data")) {
                    strArgs = JSONObject.toJSONString(args);
                }
            } catch (Exception e) {
                try {
                    strArgs = Arrays.toString(args);
                } catch (Exception ex) {
                    log.warn("解析请求内容异常", ex);
                }
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
        });
    }

    /**
     * @SysLog 注解的方法返回后
     * 设置日志状态
     * 发布事件
     */
    @AfterReturning(pointcut = "sysLogAspect()")
    public void afterReturning() {
        tryCatch((bbb) -> {
            // TODO 设置日志状态 OPT / EX


            OptLogDTO optLogDTO = getOptLogDTO();
            publishEvent(optLogDTO);
        });
    }

    /**
     * @param e
     * @SysLog 注解的方法如果出现异常
     * 设置日志状态
     * 封装异常信息
     * 发布事件
     */
    @AfterThrowing(pointcut = "sysLogAspect()", throwing = "e")
    public void AfterThrowing(Throwable e) {
        tryCatch((ccc) -> {
            OptLogDTO optLogDTO = getOptLogDTO();
            // 设置日志类型为异常(EX)
            optLogDTO.setType("EX");
            // 异常对象 => 异常堆栈信息
            optLogDTO.setExDetail(LogUtils.getStackTrace(e));
            // 异常信息
            optLogDTO.setExDesc(e.getMessage());
            // 发布事件
            publishEvent(optLogDTO);
        });
    }

    /**
     * 发布事件
     *
     * @param optLogDTO
     */
    public void publishEvent(OptLogDTO optLogDTO) {
        // 完成时间
        optLogDTO.setFinishTime(LocalDateTime.now());
        // 消费时间 开始时间 => (消费时间) => 完成时间
        optLogDTO.setConsumingTime(optLogDTO.getStartTime().until(optLogDTO.getFinishTime(), ChronoUnit.MILLIS));
        // 发布事件
        applicationContext.publishEvent(new SysLogEvent(optLogDTO));
        // 删除本地线程中的日志实体类
        THREAD_LOCAL.remove();
    }

    private void tryCatch(Consumer<String> consumer) {
        try {
            consumer.accept("");
        } catch (Exception e) {
            log.warn("记录操作日志异常", e);
            THREAD_LOCAL.remove();
        }
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
