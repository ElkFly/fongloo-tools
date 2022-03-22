package com.fongloo.auth.server.utils;

import com.fongloo.auth.server.properties.AuthServerProperties;
import com.fongloo.auth.utils.JwtHelper;
import com.fongloo.auth.utils.JwtUserInfo;
import com.fongloo.auth.utils.Token;
import lombok.AllArgsConstructor;

/**
 * JwtToken工具
 */
@AllArgsConstructor
public class JwtTokenServerUtils {
    /**
     * 认证服务端使用
     * 用于生成和解析Token
     */
    private AuthServerProperties authServerProperties;

    /**
     * 生成Token
     *
     * @param jwtUserInfo
     * @param expire
     * @return
     */
    public Token generateUserToken(JwtUserInfo jwtUserInfo, Integer expire) {
        AuthServerProperties.TokenInfo userTokenInfo = authServerProperties.getUser();
        if (expire == null || expire <= 0) {
            expire = userTokenInfo.getExpire();
        }
        return JwtHelper.generateUserToken(jwtUserInfo, userTokenInfo.getPriKey(), expire);
    }
}
