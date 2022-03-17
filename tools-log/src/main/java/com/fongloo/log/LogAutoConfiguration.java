package com.fongloo.log;

import com.fongloo.log.aspect.SysLogAspect;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 日志文件配置
 *
 * 启动条件:
 *         1. 存在web环境
 *         2. 配置文件中log.enabled = true
 */

@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "log.enabled", havingValue = "true", matchIfMissing = false)
public class LogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SysLogAspect sysLogAspect() {
        return new SysLogAspect();
    }
}
