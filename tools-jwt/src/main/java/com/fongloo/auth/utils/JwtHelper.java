package com.fongloo.auth.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JwtHelper {

    private static final RsaKeyHelper RSA_KEY_HELPER = new RsaKeyHelper();

    /**
     * 获取Token中的用户信息
     * @param token
     * @param pubKeyPath
     * @return
     */
    public static JwtUserInfo getJwtFromToken(String token, String pubKeyPath) {
        Jws<Claims> claimsJws = parserToken(token, pubKeyPath);

        Claims body = claimsJws.getBody();
        String strUserId = body.getSubject();

        // TODO 这些常用值应该封装为常量对象
        String account = body.get("account") == null ? "" : body.get("account").toString();
        String name = body.get("name") == null ? "" : body.get("name").toString();
        String strOrgId = body.get("orgid") == null ? "" : body.get("orgid").toString();
        String strDepartmentId = body.get("stationid") == null ? "" : body.get("stationid").toString();

        Long userId = longValueOf0(strUserId);
        Long orgId = longValueOf0(strOrgId);
        Long departmentId = longValueOf0(strDepartmentId);

        return new JwtUserInfo(userId, account, name, orgId, departmentId);
    }

    /**
     * String类型转换为Long类型
     * @param value
     * @return
     */
    private static Long longValueOf0(String value) {
        try {
            return Long.valueOf(value);
        } catch (Exception ex) {
            return 0L;
        }
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
        return null;
    }
}
