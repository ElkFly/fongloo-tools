package com.fongloo.utils;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Spring 工具类
 * 获取Bean或者ApplicationContext会进行非空判断
 */
public class SpringUtils {
    @Getter
    private static ApplicationContext applicationContext;
    private static ApplicationContext parentApplicationContext;

    public static void setApplicationContext(ApplicationContext ctx) {
        Assert.notNull(ctx, "SpringUtil: 注入 ApplicationContext 对象为空");
        applicationContext = ctx;
        parentApplicationContext = ctx.getParent();
    }

    public static Object getBean(String name) {
        Assert.hasText(name, "SpringUtil: name 为 null 或为空");
        try {
            return applicationContext.getBean(name);
        } catch (Exception ex) {
            return parentApplicationContext.getBean(name);
        }
    }

    public static <T> T getBean(String name, Class<T> type) {
        Assert.hasText(name, "SpringUtil: name 为 null 或为空");
        Assert.notNull(type, "SpringUtil: type 为 null");
        try {
            return applicationContext.getBean(name, type);
        } catch (Exception ex) {
            return parentApplicationContext.getBean(name, type);
        }
    }

    public static <T> T getBean(Class<T> type) {
        Assert.notNull(type, "SpringUtil: type 为 null");
        try {
            return applicationContext.getBean(type);
        } catch (Exception e) {
            return parentApplicationContext.getBean(type);
        }
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        Assert.notNull(type, "SpringUtil: type 为 null");
        try {
            return applicationContext.getBeansOfType(type);
        } catch (Exception e) {
            return parentApplicationContext.getBeansOfType(type);
        }
    }
}
