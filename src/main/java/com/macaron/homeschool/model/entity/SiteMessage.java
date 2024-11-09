package com.macaron.homeschool.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.macaron.homeschool.common.base.BaseIncrIDEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName site_message
 */
@TableName(value ="site_message")
@Data
public class SiteMessage extends BaseIncrIDEntity implements Serializable {

    private Long classId;

    private Long senderId;

    private Long recipientId;

    private String title;

    private String content;

    private static final long serialVersionUID = 1L;
}