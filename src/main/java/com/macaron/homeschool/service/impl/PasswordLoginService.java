package com.macaron.homeschool.service.impl;

import com.macaron.homeschool.common.context.UserHelper;
import com.macaron.homeschool.common.enums.GlobalServiceStatusCode;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import com.macaron.homeschool.common.jwt.JwtUtil;
import com.macaron.homeschool.common.util.PasswordUtil;
import com.macaron.homeschool.model.dto.UserLoginDTO;
import com.macaron.homeschool.model.dto.UserPasswordLoginDTO;
import com.macaron.homeschool.model.entity.User;
import com.macaron.homeschool.model.vo.UserLoginVO;
import com.macaron.homeschool.service.LoginService;
import com.macaron.homeschool.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 11:32
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordLoginService implements LoginService {

    private final UserService userService;

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        UserPasswordLoginDTO passwordParams = Optional.ofNullable(userLoginDTO.getPasswordParams()).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.PARAM_IS_BLANK));
        // 获取数据库的用户数据
        User dbUser = userService.getUserByUsername(passwordParams.getUsername()).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.USER_USERNAME_PASSWORD_ERROR));
        // 判断是否可以登录
        checkCanLogin(dbUser);
        // 校验
        if(PasswordUtil.confirm(passwordParams.getPassword(), dbUser.getPassword())) {
            // 封装结果
            UserHelper userHelper = UserHelper.builder()
                    .userId(dbUser.getId())
                    .role(dbUser.getUserType())
                    .build();
            return UserLoginVO.builder()
                    .token(JwtUtil.createJwt(userHelper))
                    .build();
        }else {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_USERNAME_PASSWORD_ERROR);
        }
    }

}
