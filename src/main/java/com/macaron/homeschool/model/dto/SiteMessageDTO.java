package com.macaron.homeschool.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 23:16
 */
@Data
public class SiteMessageDTO {

    @NotNull(message = "班级 id 不能为空")
    private Long classId;

    @NotNull(message = "接受者 id 不能为空")
    private Long recipientId;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

}
