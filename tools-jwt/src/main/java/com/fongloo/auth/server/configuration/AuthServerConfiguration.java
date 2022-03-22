package com.fongloo.auth.server.configuration;

import com.fongloo.auth.server.properties.AuthServerProperties;
import com.fongloo.auth.server.utils.JwtTokenServerUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 认证服务配置
 */
@EnableConfigurationProperties({AuthServerProperties.class})
public class AuthServerConfiguration {
    public JwtTokenServerUtils getJwtTokenServerUtils(AuthServerProperties authServerProperties) {
        return new JwtTokenServerUtils(authServerProperties);
    }
}
