package com.macaron.homeschool.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 15:51
 */
@Data
public class SystemMessageVO {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createTime;

}
