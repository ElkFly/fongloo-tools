package com.fongloo.auth.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * JWT 存储的内容
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserInfo implements Serializable {
    /**
     * 账号id
     */
    private Long userId;

    /**
     * 账号
     */
    private String account;

    /**
     * 姓名
     */
    private String name;

    /**
     * 当前人登录单位ID
     */
    private Long orgId;

    /**
     * 当前登录人岗位ID
     */
    private Long stationId;
}
