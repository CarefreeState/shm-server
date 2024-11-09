package com.macaron.homeschool.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-07
 * Time: 15:37
 */
@Data
public class SchoolClassDetailVO {

    private Long id;

    private String className;

    private Integer auditStatus;

    private UserVO creator;

    private List<SchoolClassUserVO> userList;
}
