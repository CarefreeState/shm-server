package com.macaron.homeschool.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 22:40
 */
@Data
public class ClassMessageQueryDTO {

    private Integer current;

    private Integer pageSize;

    @NotNull(message = "班级 id 不能为空")
    private Long classId;

    private Boolean isFromMe;

}
