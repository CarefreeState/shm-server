package com.macaron.homeschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macaron.homeschool.common.enums.AuditStatus;
import com.macaron.homeschool.common.enums.UserType;
import com.macaron.homeschool.model.dto.UserRegisterDTO;
import com.macaron.homeschool.model.entity.User;
import com.macaron.homeschool.model.vo.UserVO;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【user(用户基本信息表)】的数据库操作Service
* @createDate 2024-11-05 21:06:05
*/
public interface UserService extends IService<User> {

    Optional<User> getUserById(Long userId);

    Optional<User> getUserByUsername(String username);

    User register(UserRegisterDTO userRegisterDTO);

    User checkAndGetUserById(Long userId);

    List<UserVO> getUserList(List<UserType> userTypes);

    void auditUser(Long userId, AuditStatus auditStatus);

}
