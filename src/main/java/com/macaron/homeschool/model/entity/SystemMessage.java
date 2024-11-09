package com.macaron.homeschool.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.macaron.homeschool.common.base.BaseIncrIDEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName system_message
 */
@TableName(value ="system_message")
@Data
public class SystemMessage extends BaseIncrIDEntity implements Serializable {

    private Long creatorId;

    private String title;

    private String content;

    private static final long serialVersionUID = 1L;
}