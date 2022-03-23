package com.fongloo.user.feign;

import lombok.*;

/**
 * 用户查询
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserQuery {
    /**
     * 是否查询SysUser对象的所有信息, true则通过RPC接口查询
     */
    private Boolean full;

    /**
     * 是否只查询角色信息, true则通过RPC接口查询
     */
    private Boolean roles;

    /**
     * 是否只查询组织信息, true则通过RPC接口查询
     */
    private Boolean org;

    /**
     * 是否只查询岗位信息, true则通过RPC接口查询
     */
    private Boolean station;
}
