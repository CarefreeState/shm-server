package com.macaron.homeschool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macaron.homeschool.common.enums.AuditStatus;
import com.macaron.homeschool.model.dao.mapper.ClassUserLinkMapper;
import com.macaron.homeschool.model.entity.ClassUserLink;
import com.macaron.homeschool.service.ClassUserLinkService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【class_teacher_link(班级-用户关联表)】的数据库操作Service实现
* @createDate 2024-11-05 21:06:05
*/
@Service
public class ClassUserLinkServiceImpl extends ServiceImpl<ClassUserLinkMapper, ClassUserLink>
    implements ClassUserLinkService {

    @Override
    public Optional<ClassUserLink> getClassUserLink(Long classId, Long userId) {
        return this.lambdaQuery()
                .eq(ClassUserLink::getClassId, classId)
                .eq(ClassUserLink::getUserId, userId)
                .oneOpt();
    }

    @Override
    public void createClassUserLink(Long classId, Long userId) {
        ClassUserLink classUserLink = new ClassUserLink();
        classUserLink.setClassId(classId);
        classUserLink.setUserId(userId);
        this.save(classUserLink);
    }

    @Override
    public void auditClassUser(Long classId, Long userId, AuditStatus auditStatus) {
        this.lambdaUpdate()
                .eq(ClassUserLink::getClassId, classId)
                .eq(ClassUserLink::getUserId, userId)
                .set(ClassUserLink::getAuditStatus, auditStatus.getCode())
                .update();
    }
}




