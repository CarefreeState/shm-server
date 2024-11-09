package com.macaron.homeschool.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-09
 * Time: 1:30
 */
@Data
public class SchoolClassAboutMeVO {

    private Long id;

    private String className;

    private Boolean isMine;

    private Integer classAuditStatus;

    private Integer attendAuditStatus;

    private LocalDateTime createTime;

}
