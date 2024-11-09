package com.macaron.homeschool.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 15:13
 */
@Data
public class AuditUserDTO {

    @NotNull(message = "用户 id 不能为空")
    private Long userId;

    @NotNull(message = "审核状态不能为空")
    private Integer auditStatus;

}
