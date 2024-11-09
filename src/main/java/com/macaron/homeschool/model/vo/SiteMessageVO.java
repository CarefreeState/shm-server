package com.macaron.homeschool.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 23:29
 */
@Data
public class SiteMessageVO {

    private Long id;

    private UserVO sender;

    private UserVO recipient;

    private String title;

    private String content;

    private LocalDateTime createTime;

}
