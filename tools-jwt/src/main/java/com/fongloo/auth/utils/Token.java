package com.fongloo.auth.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token implements Serializable {
    private static final Long serialVerSionUID = Long.MIN_VALUE;

    /**
     * token
     */
    @ApiModelProperty("token")
    private String token;

    /**
     * 有效时间 单位: 秒
     */
    @ApiModelProperty(value = "有效期", notes = "单位: 秒")
    private Integer expire;
}
