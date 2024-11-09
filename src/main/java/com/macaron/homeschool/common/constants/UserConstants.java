package com.macaron.homeschool.common.constants;

import com.macaron.homeschool.common.enums.UserType;

import java.util.Set;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 10:57
 */
public interface UserConstants {

    String USER_REGISTER_LOCK = "userRegisterLock:";

    Set<UserType> NO_AUDIT_REQUIRED_ROLES = Set.of(UserType.GUARDIAN);

    String MYSELF = "我";

}
