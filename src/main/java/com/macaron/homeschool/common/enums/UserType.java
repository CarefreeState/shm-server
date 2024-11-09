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
 * Time: 21:16
 */
@Getter
@AllArgsConstructor
public enum UserType {

    MANAGER(1, "manager"),
    TEACHER(2, "teacher"),
    GUARDIAN(3, "guardian"),
    ;

    @JsonValue // OpenAPI 识别枚举默认是字符串类型，即实例名，标注 @JsonValue 后，code 仍被认为是字符串
    @EnumValue
    private final Integer code;

    private final String name;

    public static UserType get(Integer role) {
        for (UserType userType : UserType.values()) {
            if(userType.getCode().equals(role)) {
                return userType;
            }
        }
        throw new GlobalServiceException(GlobalServiceStatusCode.USER_TYPE_EXCEPTION);
    }

}
