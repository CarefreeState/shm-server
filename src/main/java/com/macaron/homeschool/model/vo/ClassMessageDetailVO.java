package com.macaron.homeschool.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 22:18
 */
@Data
public class ClassMessageDetailVO {

    private Long id;

    private UserInfoVO userInfoVO;

    private String title;

    private String content;

    private LocalDateTime createTime;

}
