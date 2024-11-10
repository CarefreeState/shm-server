package com.macaron.homeschool.model.converter;

import com.macaron.homeschool.model.dto.AuditUserDTO;
import com.macaron.homeschool.model.dto.UserRegisterDTO;
import com.macaron.homeschool.model.entity.User;
import com.macaron.homeschool.model.vo.UserInfoVO;
import com.macaron.homeschool.model.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 11:09
 */
@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "auditStatus", expression = "java(com.macaron.homeschool.common.enums.AuditStatus.get(auditUserDTO.getAuditStatus()))")
    User auditUserDTOToUser(AuditUserDTO auditUserDTO);

    @Mapping(target = "userType", expression = "java(com.macaron.homeschool.common.enums.UserType.get(userRegisterDTO.getUserType()))")
    User userRegisterDTOToUser(UserRegisterDTO userRegisterDTO);

    @Mapping(target = "userType", expression = "java(java.util.Optional.ofNullable(user.getUserType()).map(com.macaron.homeschool.common.enums.UserType::getCode).orElse(null))")
    UserInfoVO userToUserInfoVO(User user);

    @Mapping(target = "userType", expression = "java(java.util.Optional.ofNullable(user.getUserType()).map(com.macaron.homeschool.common.enums.UserType::getCode).orElse(null))")
    @Mapping(target = "auditStatus", expression = "java(java.util.Optional.ofNullable(user.getAuditStatus()).map(com.macaron.homeschool.common.enums.AuditStatus::getCode).orElse(null))")
    UserVO userToUserVO(User user);

    List<UserVO> userListToUserVOList(List<User> userList);

}
