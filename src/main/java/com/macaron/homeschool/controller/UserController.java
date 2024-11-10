package com.macaron.homeschool.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.macaron.homeschool.common.SystemJsonResponse;
import com.macaron.homeschool.common.annotation.Intercept;
import com.macaron.homeschool.common.context.BaseContext;
import com.macaron.homeschool.common.enums.GlobalServiceStatusCode;
import com.macaron.homeschool.common.enums.UserType;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import com.macaron.homeschool.model.converter.UserConverter;
import com.macaron.homeschool.model.dto.AuditUserDTO;
import com.macaron.homeschool.model.dto.UserLoginDTO;
import com.macaron.homeschool.model.dto.UserRegisterDTO;
import com.macaron.homeschool.model.entity.User;
import com.macaron.homeschool.model.vo.UserInfoVO;
import com.macaron.homeschool.model.vo.UserLoginVO;
import com.macaron.homeschool.model.vo.UserVO;
import com.macaron.homeschool.service.LoginService;
import com.macaron.homeschool.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 11:47
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "用户测试接口")
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    @Intercept(ignore = true)
    public SystemJsonResponse<UserLoginVO> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        String serviceName = userLoginDTO.getLoginType() + LoginService.BASE_NAME;
        LoginService service = SpringUtil.getBean(serviceName, LoginService.class);
        UserLoginVO userLoginVO = service.login(userLoginDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(userLoginVO);
    }

    @GetMapping("/info")
    @Operation(summary = "读取当前用户信息")
    @Intercept(permit = {UserType.MANAGER, UserType.TEACHER, UserType.GUARDIAN})
    public SystemJsonResponse<UserInfoVO> getUserInfo() {
        Long userId = BaseContext.getCurrentUser().getUserId();
        UserInfoVO userInfoVO = userService.getUserInfoVOById(userId);
        return SystemJsonResponse.SYSTEM_SUCCESS(userInfoVO);
    }

    @GetMapping("/info/{userId}")
    @Operation(summary = "读取指定用户信息")
    @Intercept(permit = {UserType.MANAGER, UserType.TEACHER, UserType.GUARDIAN})
    public SystemJsonResponse<UserInfoVO> getUserInfoByUserId(@PathVariable("userId") @NotNull(message = "用户 id 不能为空") Long userId) {
        UserInfoVO userInfoVO = userService.getUserInfoVOById(userId);
        return SystemJsonResponse.SYSTEM_SUCCESS(userInfoVO);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    @Intercept(ignore = true)
    public SystemJsonResponse<UserInfoVO> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        User user = userService.register(userRegisterDTO);
        UserInfoVO userInfoVO = UserConverter.INSTANCE.userToUserInfoVO(user);
        return SystemJsonResponse.SYSTEM_SUCCESS(userInfoVO);
    }

    @GetMapping("/list/teacher")
    @Operation(summary = "获取老师列表")
    @Intercept(permit = {UserType.MANAGER})
    public SystemJsonResponse<List<UserVO>> getTeacherList() {
        List<UserVO> userList = userService.getUserList(List.of(UserType.TEACHER));
        return SystemJsonResponse.SYSTEM_SUCCESS(userList);
    }

    @PutMapping("/audit/user")
    @Operation(summary = "审核用户")
    @Intercept(permit = {UserType.MANAGER})
    public SystemJsonResponse<?> auditUser(@Valid @RequestBody AuditUserDTO auditUserDTO) {
        Long userId = auditUserDTO.getUserId();
        if(BaseContext.getCurrentUser().getUserId().equals(userId)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }
        userService.auditUser(auditUserDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }
}
