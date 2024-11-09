package com.macaron.homeschool.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 16:43
 */
@Data
public class SchoolClassVO {

    private Long id;

    private String className;

    private Integer auditStatus;

    private LocalDateTime createTime;

}
