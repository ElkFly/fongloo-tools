package com.fongloo.log.utils;

import com.fongloo.log.annotation.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

/**
 * 日志工具类
 */
@Slf4j
public class LogUtils {

    /**
     * 获取加上@SysLog注解的方法的参数信息
     *
     * @param joinPoint
     * @return
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) {
        // 获取连接点目标类名
        String className = joinPoint.getTarget().getClass().getName();
        try {
            // 获取连接点签名的方法名
            String methodName = joinPoint.getSignature().getName();
            // 获取连接点参数
            Object[] args = joinPoint.getArgs();
            // 根据连接点类的名称获取指定类
            Class<?> clazz = Class.forName(className);
            // 获取类中的方法
            Method[] methods = clazz.getMethods();
            String description = "";
            for (Method method : methods) {
                if (method.equals(methodName)) {
                    // 获取其中的参数类型
                    Class<?>[] parameter = method.getParameterTypes();
                    if (parameter.length == args.length) {
                        description = method.getAnnotation(SysLog.class).value();
                        break;
                    }
                }
            }
            return description;
        } catch (ClassNotFoundException e) {
            log.warn("找不到这个类: {}", className);
            return "";
        }
    }


    /**
     * 获取堆栈信息
     *
     * @param throwable 异常
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
