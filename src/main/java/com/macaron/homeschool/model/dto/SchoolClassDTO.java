package com.macaron.homeschool.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 16:23
 */
@Data
public class SchoolClassDTO {

    @NotBlank(message = "班级名称不能为 null")
    private String className;

}
