package com.macaron.homeschool.service;

import com.macaron.homeschool.common.enums.AuditStatus;
import com.macaron.homeschool.common.enums.GlobalServiceStatusCode;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import com.macaron.homeschool.model.dto.UserLoginDTO;
import com.macaron.homeschool.model.entity.User;
import com.macaron.homeschool.model.vo.UserLoginVO;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 11:22
 */
public interface LoginService {

    String BASE_NAME = "LoginService";

    UserLoginVO login(UserLoginDTO userLoginDTO);

    default void checkCanLogin(User dbUser) {
        // 判断是否通过了审核
        if(!dbUser.getAuditStatus().equals(AuditStatus.AUDIT_PASSED)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.AUDIT_STATUS_NOT_APPROVED);
        }
    }

}
