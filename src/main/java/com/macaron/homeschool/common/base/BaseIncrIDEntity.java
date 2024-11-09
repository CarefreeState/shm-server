package com.macaron.homeschool.common.base;

import com.baomidou.mybatisplus.annotation.*;
import com.macaron.homeschool.common.constants.DateTimeConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <span>
 * classes that inherit this entity all have auto-incrementing ID.
 * </span>
 *
 */
@Getter
@Setter
public class BaseIncrIDEntity implements Serializable {

    /**
     * id, incr
     */
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = DateTimeConstants.DATE_TIME_PATTERN)
    protected LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = DateTimeConstants.DATE_TIME_PATTERN)
    private LocalDateTime updateTime;

    /**
     * 乐观锁
     */
    @TableField(value = "version", fill = FieldFill.INSERT)
    @Version
    private Integer version;

    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

}
