package com.macaron.homeschool.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 11:29
 */
@Data
public class UserLoginDTO {

    /**
     * 登录方式
     */
    @NotBlank(message = "登录方式不能为空")
    private String loginType;


    /**
     * 密码登录
     */
    @Valid
    private UserPasswordLoginDTO passwordParams;

}
