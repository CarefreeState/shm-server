package com.macaron.homeschool.common.annotation;


import com.macaron.homeschool.common.enums.UserType;

import java.lang.annotation.*;

/**
 * Created With Intellij IDEA
 * User: 马拉圈
 * Date: 2024-08-08
 * Time: 12:50
 * Description: 此注解用于辅助判断账户访问一个接口是否拦截
 * permit 代表允许访问接口方法的角色
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Intercept {

    UserType[] permit() default {};

    boolean ignore() default false;

}
