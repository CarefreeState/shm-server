package com.macaron.homeschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macaron.homeschool.common.enums.AuditStatus;
import com.macaron.homeschool.model.entity.ClassUserLink;

import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【class_teacher_link(班级-用户关联表)】的数据库操作Service
* @createDate 2024-11-05 21:06:05
*/
public interface ClassUserLinkService extends IService<ClassUserLink> {

    Optional<ClassUserLink> getClassUserLink(Long classId, Long userId);

    void createClassUserLink(Long classId, Long userId);

    void auditClassUser(Long classId, Long userId, AuditStatus auditStatus);

}
