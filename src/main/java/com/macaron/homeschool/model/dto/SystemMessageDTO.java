package com.macaron.homeschool.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 15:37
 */
@Data
public class SystemMessageDTO {

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

}
