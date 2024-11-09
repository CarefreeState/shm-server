package com.macaron.homeschool.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-05
 * Time: 22:24
 */
@Getter
@AllArgsConstructor
public enum AuditStatus {

    NOT_AUDITED(0, "未审核"),
    AUDIT_PASSED(1, "审核通过"),
    AUDIT_FAILED(2, "审核不通过"),
    ;

    @JsonValue
    @EnumValue
    private final Integer code;

    private final String description;

    public static AuditStatus get(Integer role) {
        for (AuditStatus auditStatus : AuditStatus.values()) {
            if(auditStatus.getCode().equals(role)) {
                return auditStatus;
            }
        }
        throw new GlobalServiceException(GlobalServiceStatusCode.AUDIT_STATUS_EXCEPTION);
    }

}
