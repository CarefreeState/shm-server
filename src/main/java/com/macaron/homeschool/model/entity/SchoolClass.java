package com.macaron.homeschool.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.macaron.homeschool.common.base.BaseIncrIDEntity;
import com.macaron.homeschool.common.enums.AuditStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName school_class
 */
@TableName(value ="school_class")
@Data
public class SchoolClass extends BaseIncrIDEntity implements Serializable {

    private Long creatorId;

    private String className;

    private AuditStatus auditStatus;

    private static final long serialVersionUID = 1L;
}