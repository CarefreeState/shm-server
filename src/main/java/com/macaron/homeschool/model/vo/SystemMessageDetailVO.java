package com.macaron.homeschool.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 15:54
 */
@Data
public class SystemMessageDetailVO {

    private Long id;

    private UserInfoVO userInfoVO;

    private String title;

    private String content;

    private LocalDateTime createTime;

}
