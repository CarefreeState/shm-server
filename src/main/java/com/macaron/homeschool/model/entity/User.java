package com.macaron.homeschool.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.macaron.homeschool.common.base.BaseIncrIDEntity;
import com.macaron.homeschool.common.enums.AuditStatus;
import com.macaron.homeschool.common.enums.UserType;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User extends BaseIncrIDEntity implements Serializable {

    private String username;

    private String password;

    private String nickname;

    private String phoneNumber;

    private UserType userType;

    private AuditStatus auditStatus;

    private static final long serialVersionUID = 1L;
}