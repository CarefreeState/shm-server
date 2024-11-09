package com.macaron.homeschool.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.macaron.homeschool.common.base.BaseIncrIDEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName class_message
 */
@TableName(value ="class_message")
@Data
public class ClassMessage extends BaseIncrIDEntity implements Serializable {

    private Long creatorId;

    private Long classId;

    private String title;

    private String content;

    private static final long serialVersionUID = 1L;
}