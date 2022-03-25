package com.fongloo.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.time.LocalDateTime;

/**
 * 派生类基础实体
 *
 * @param <T>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
public class SuperEntity<T> {
    public static final String FIELD_ID = "id";
    public static final String CREATE_TIME = "createTime";
    public static final String CREATE_USER = "createUser";

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.INPUT) // 手动赋值
    @NotNull(message = "id不能为空", groups = SuperEntity.Update.class)
    protected T id;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @ApiModelProperty("创建人ID")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    protected T createUser;

    @Override
    protected Object clone() {
        // 浅克隆
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO 这里应该抛出自定义异常
            return new CloneNotSupportedException();
        }
    }

    /**
     * 更新和缺省校验组
     */
    public interface Update extends Default {
    }

    /**
     * 保存和缺省验证组
     */
    public interface Save extends Default {
    }
}
