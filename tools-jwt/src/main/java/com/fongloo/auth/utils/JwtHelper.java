package com.fongloo.auth.utils;

import cn.hutool.core.date.DateUnit;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
public class JwtHelper {

    private static final RsaKeyHelper RSA_KEY_HELPER = new RsaKeyHelper();

    /**
     * 获取Token中的用户信息
     *
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
     *
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

    /**
     * 根据用户信息生成Token
     *
     * @param jwtUserInfo 用户信息
     * @param priKey      私钥
     * @param expire      有效期
     * @return
     */
    public static Token generateUserToken(JwtUserInfo jwtUserInfo, String priKey, Integer expire) {
        // TODO 此处的值应为常量 所以需要封装一个常量对象
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(String.valueOf(jwtUserInfo.getUserId()))
                .claim("account", jwtUserInfo.getAccount())
                .claim("name", jwtUserInfo.getName())
                .claim("orgId", jwtUserInfo.getOrgId())
                .claim("stationid", jwtUserInfo.getStationId());
        return generateToken(jwtBuilder, priKey, expire);
    }

    /**
     * 生成Token
     *
     * @param jwtBuilder
     * @param priKeyPath
     * @param expire
     * @return
     */
    private static Token generateToken(JwtBuilder jwtBuilder, String priKeyPath, Integer expire) {
        try {
            String strToken = jwtBuilder
                    // 设置有效时间
                    .setExpiration(localDateTimeToDate(LocalDateTime.now().plusSeconds(expire)))
                    // 设置加密算法
                    .signWith(SignatureAlgorithm.RS256, RSA_KEY_HELPER.getPrivateKey(priKeyPath))
                    .compact();

            // token
            return new Token(strToken, expire);

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            // TODO 此处应该打印错误信息，并抛出自定义异常
        }
        return null;
    }

    /**
     * LocalDateTime对象转换为Date对象
     *
     * @param localDateTime
     * @return
     */
    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

}
