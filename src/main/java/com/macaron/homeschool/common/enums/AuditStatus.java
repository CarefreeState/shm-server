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

    /**
     * 建议枚举统一 【输入和输出】 依旧是 code 的类型，并交给数据校验层前校验，数据映射层去映射，这样情况会更加可控
     * （不同框架的映射逻辑还不一样，比如 MP 若 code 为 null 会映射一个默认值）
     * 也还原真实的请求和响应数据结构，也更加容易观察，生成的接口文档类型也不会有问题
     */
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
