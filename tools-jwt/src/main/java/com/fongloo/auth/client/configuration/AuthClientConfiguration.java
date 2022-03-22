package com.fongloo.auth.client.configuration;

import com.fongloo.auth.client.properties.AuthClientProperties;
import com.fongloo.auth.client.utils.JwtTokenClientUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 客户端配置
 */
@EnableConfigurationProperties(AuthClientProperties.class)
public class AuthClientConfiguration {
    @Bean
    public JwtTokenClientUtils getJwtTokenClientUtils(AuthClientProperties authClientProperties) {
        return new JwtTokenClientUtils(authClientProperties);
    }
}
