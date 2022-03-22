package com.fongloo.auth.server.configuration;

import com.fongloo.auth.server.properties.AuthServerProperties;
import com.fongloo.auth.server.utils.JwtTokenServerUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 认证服务配置
 */
@EnableConfigurationProperties({AuthServerProperties.class})
public class AuthServerConfiguration {
    @Bean
    public JwtTokenServerUtils getJwtTokenServerUtils(AuthServerProperties authServerProperties) {
        return new JwtTokenServerUtils(authServerProperties);
    }
}
