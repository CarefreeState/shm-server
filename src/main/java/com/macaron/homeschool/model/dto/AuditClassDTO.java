package com.macaron.homeschool.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 21:50
 */
@Data
public class AuditClassDTO {

    @NotNull(message = "班级 id 不能为空")
    Long classId;

    @NotNull(message = "审核状态不能为空")
    Integer auditStatus;

}
