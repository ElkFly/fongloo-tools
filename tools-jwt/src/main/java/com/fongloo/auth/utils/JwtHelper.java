package com.fongloo.auth.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JwtHelper {
    private static final RsaKeyHelper RSA_KEY_HELPER = new RsaKeyHelper();

    public static JwtUserInfo getJwtFromToken(String token, String pubKeyPath) {
        Jws<Claims> claimsJws = parserToken(token, pubKeyPath);
        Claims body = claimsJws.getBody();
        String userId = body.getSubject();

    }

    /**
     * 公钥解析 => Token
     * 从PublicKey对象中解析出Token
     *
     * @param token
     * @param pubKeyPath 公钥路径
     * @return
     */
    private static Jws<Claims> parserToken(String token, String pubKeyPath) {
        try {

            return Jwts.parser().
                    setSigningKey(RSA_KEY_HELPER.getPublicKey(pubKeyPath))
                    .parseClaimsJws(token);

        } catch (IOException ex) {
            log.error("找不到 [ {} ] 文件", pubKeyPath);
        } catch (ExpiredJwtException ex) {
            // TODO  过期

        } catch (SignatureException ex) {
            // TODO 签名错误

        } catch (IllegalArgumentException ex) {
            // TODO 前面为空

        } catch (Exception ex) {
            // TODO 解析Token失败
//            log.error("错误代码: {}, 错误信息: {}",);
        }
    }
}
