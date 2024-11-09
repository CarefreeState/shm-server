package com.macaron.homeschool.model.dto;

import com.macaron.homeschool.common.annotation.UserTypeIn;
import com.macaron.homeschool.common.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 0:15
 */
@Data
public class UserRegisterDTO {

    @NotBlank(message = "用户名不能空")
    private String username;

    @NotBlank(message = "昵称不能空")
    private String nickname;

    @NotBlank(message = "密码不能空")
    private String password;

    @NotNull(message = "用户类型不能为空")
    @UserTypeIn(role = {UserType.TEACHER, UserType.GUARDIAN})
    private Integer userType;

}
