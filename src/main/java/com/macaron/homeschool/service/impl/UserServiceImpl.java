package com.macaron.homeschool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macaron.homeschool.common.constants.UserConstants;
import com.macaron.homeschool.common.enums.AuditStatus;
import com.macaron.homeschool.common.enums.GlobalServiceStatusCode;
import com.macaron.homeschool.common.enums.UserType;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import com.macaron.homeschool.common.util.PasswordUtil;
import com.macaron.homeschool.model.converter.UserConverter;
import com.macaron.homeschool.model.dao.mapper.UserMapper;
import com.macaron.homeschool.model.dto.UserRegisterDTO;
import com.macaron.homeschool.model.entity.User;
import com.macaron.homeschool.model.vo.UserInfoVO;
import com.macaron.homeschool.model.vo.UserVO;
import com.macaron.homeschool.redis.cache.RedisCache;
import com.macaron.homeschool.redis.lock.RedisLock;
import com.macaron.homeschool.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【user(用户基本信息表)】的数据库操作Service实现
* @createDate 2024-11-05 21:06:05
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private final RedisCache redisCache;

    private final RedisLock redisLock;

    @Override
    public Optional<User> getUserById(Long userId) {
        return this.lambdaQuery()
                .eq(User::getId, userId)
                .oneOpt();
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return this.lambdaQuery()
                .eq(User::getUsername, username)
                .oneOpt();
    }

    @Override
    public User register(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUsername();
        return redisLock.tryLockGetSomething(UserConstants.USER_REGISTER_LOCK + username, () -> {
            getUserByUsername(username).ifPresent(account -> {
                throw new GlobalServiceException(String.format("账户 %s 已存在，注册失败", username), GlobalServiceStatusCode.USER_ACCOUNT_ALREADY_EXIST);
            });
            // 注册用户
            User user = UserConverter.INSTANCE.userRegisterDTOToUser(userRegisterDTO);
            if(UserConstants.NO_AUDIT_REQUIRED_ROLES.contains(user.getUserType())) {
                user.setAuditStatus(AuditStatus.AUDIT_PASSED);
            }
            // 加盐加密
            user.setPassword(PasswordUtil.encrypt(userRegisterDTO.getPassword()));
            this.save(user);
            log.info("用户注册成功：{}", user);
            return user;
        }, () -> null);
    }

    @Override
    public User checkAndGetUserById(Long userId) {
        String redisKey = UserConstants.USER_INFO_MAP + userId;
        return redisCache.<User>getCacheObject(redisKey).orElseGet(() -> {
            User user = getUserById(userId).orElseThrow(() ->
                    new GlobalServiceException(GlobalServiceStatusCode.USER_ACCOUNT_NOT_EXIST));
            redisCache.setCacheObject(redisKey, user, UserConstants.USER_INFO_MAP_TIMEOUT, UserConstants.USER_INFO_MAP_UNIT);
            return user;
        });
    }

    @Override
    public UserInfoVO getUserInfoVOById(Long userId) {
        return UserConverter.INSTANCE.userToUserInfoVO(checkAndGetUserById(userId));
    }

    @Override
    public UserVO getUserVOById(Long userId) {
        return UserConverter.INSTANCE.userToUserVO(checkAndGetUserById(userId));
    }

    @Override
    public List<UserVO> getUserList(List<UserType> userTypes) {
        if(CollectionUtils.isEmpty(userTypes)) {
            return new ArrayList<>();
        }
        List<User> userList = this.lambdaQuery()
                .in(User::getUserType, userTypes)
                .orderBy(Boolean.TRUE, Boolean.TRUE, User::getAuditStatus)
                .list();
        return UserConverter.INSTANCE.userListToUserVOList(userList);
    }

    @Override
    public void auditUser(Long userId, AuditStatus auditStatus) {
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setAuditStatus(auditStatus);
        this.updateById(updateUser);
    }
}




