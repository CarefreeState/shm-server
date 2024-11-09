package com.macaron.homeschool.common.annotation;

import com.macaron.homeschool.common.annotation.handler.UserTypeInValidator;
import com.macaron.homeschool.common.enums.UserType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 0:21
 */
@Documented
@Constraint(validatedBy = {UserTypeInValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserTypeIn {

    String message() default "用户类型不在可选范围内"; // 默认消息

    UserType[] role() default {}; // 可选用户类型

    Class<?>[] groups() default {}; // 分组校验

    Class<? extends Payload>[] payload() default {}; // 负载信息
}
