package com.fongloo.auth.server.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.fongloo.auth.server.properties.AuthServerProperties.PREFIX;


/**
 * 服务端配置
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = PREFIX)
public class AuthServerProperties {
    public static final String PREFIX = "authentication";


    private TokenInfo user;

    @Data
    public static class TokenInfo {
        /**
         * 过期时间
         */
        private Integer expire = 7200;

        /**
         * 加密服务使用
         */
        private String priKey;

        /**
         * 解密
         */
        private String pubKey;
    }
}
