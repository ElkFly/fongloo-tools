package com.fongloo.user.model;

import lombok.*;

/**
 * 角色
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true)
public class SysRole {
    private static final long serialVersionUID = 214123412341L;

    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 功能描述
     */
    private String describe;

    /**
     * 是否启用
     */
    private Boolean isEnable;

    /**
     * 是否是只读角色
     */
    private Boolean isReadonly;
}
