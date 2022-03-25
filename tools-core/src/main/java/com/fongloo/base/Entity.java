package com.fongloo.base;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 基础实体
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
public class Entity<T> extends SuperEntity<T> {

    public static final String UPDATE_TIME = "updateTime";
    public static final String UPDATE_USER = "updateUser";

    @ApiModelProperty("最后修改的时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;

    @ApiModelProperty("最后修改人的ID")
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    protected T updateUser;

    /**
     * @param id         用户id
     * @param createTime 创建的时间
     * @param createUser 创建的用户
     * @param updateTime 修改的时间
     * @param updateUser 修改的用户
     */
    public Entity(T id, LocalDateTime createTime, T createUser, LocalDateTime updateTime, T updateUser) {
        super(id, createTime, createUser);
        this.updateTime = updateTime;
        this.updateUser = updateUser;
    }
}
