package com.fongloo.auth.client.utils;

import com.fongloo.auth.client.properties.AuthClientProperties;
import com.fongloo.auth.utils.JwtUserInfo;
import lombok.AllArgsConstructor;

/**
 * 客户端工具
 */
@AllArgsConstructor
public class JwtTokenClientUtils {
    /**
     * 用于 认证服务的 客户端使用 （如 网关）， 在网关获取到token后，
     * 调用此工具类进行token解析
     * 客户端一般只需要解析token即可
     */
    private AuthClientProperties authClientProperties;

    /**
     * 解析Token
     * @param token
     * @return
     */
    public JwtUserInfo getUserInfo(String token) {
        AuthClientProperties.TokenInfo user = authClientProperties.getUser();

    }

}
