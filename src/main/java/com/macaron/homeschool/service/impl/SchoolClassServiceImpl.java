package com.macaron.homeschool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macaron.homeschool.common.constants.SchoolClassConstants;
import com.macaron.homeschool.common.enums.AuditStatus;
import com.macaron.homeschool.common.enums.GlobalServiceStatusCode;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import com.macaron.homeschool.model.converter.SchoolClassConverter;
import com.macaron.homeschool.model.converter.SchoolClassConverterImpl;
import com.macaron.homeschool.model.dao.mapper.SchoolClassMapper;
import com.macaron.homeschool.model.dto.AuditClassDTO;
import com.macaron.homeschool.model.dto.SchoolClassDTO;
import com.macaron.homeschool.model.entity.SchoolClass;
import com.macaron.homeschool.model.vo.SchoolClassAboutMeVO;
import com.macaron.homeschool.model.vo.SchoolClassDetailVO;
import com.macaron.homeschool.model.vo.SchoolClassVO;
import com.macaron.homeschool.redis.lock.RedisLock;
import com.macaron.homeschool.service.ClassUserLinkService;
import com.macaron.homeschool.service.SchoolClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【school_class(班级表)】的数据库操作Service实现
* @createDate 2024-11-05 21:06:05
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class SchoolClassServiceImpl extends ServiceImpl<SchoolClassMapper, SchoolClass>
    implements SchoolClassService{

    private final RedisLock redisLock;

    private final SchoolClassMapper schoolClassMapper;

    private final ClassUserLinkService classUserLinkService;

    @Override
    public Optional<SchoolClass> getSchoolClass(Long classId) {
        return this.lambdaQuery()
                .eq(SchoolClass::getId, classId)
                .oneOpt();
    }

    @Override
    public Long createSchoolClass(Long teacherId, SchoolClassDTO schoolClassDTO) {
        SchoolClass schoolClass = SchoolClassConverter.INSTANCE.schoolClassDTOToSchoolClass(schoolClassDTO);
        schoolClass.setCreatorId(teacherId);
        this.save(schoolClass);
        log.info("老师 {} 创建了班级 {}", teacherId, schoolClass);
        return schoolClass.getId();
    }

    @Override
    public SchoolClass checkAndGetSchoolClass(Long classId) {
        return getSchoolClass(classId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.SCHOOL_CLASS_NOT_EXISTS));
    }

    @Override
    public void updateSchoolClass(Long classId, Long teacherId, SchoolClassDTO schoolClassDTO) {
        SchoolClass schoolClass = SchoolClassConverter.INSTANCE.schoolClassDTOToSchoolClass(schoolClassDTO);
        SchoolClass dbSchoolClass = checkAndGetSchoolClass(classId);
        // 审核通过无法修改（并发带来不了损失）
        if(AuditStatus.AUDIT_PASSED.equals(dbSchoolClass.getAuditStatus())) {
            throw new GlobalServiceException(GlobalServiceStatusCode.AUDIT_STATUS_APPROVED);
        }
        if(!dbSchoolClass.getCreatorId().equals(teacherId)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }
        this.lambdaUpdate()
                .eq(SchoolClass::getId, classId)
                .update(schoolClass);
    }

    @Override
    public void auditSchoolClass(AuditClassDTO auditClassDTO) {
        this.updateById(SchoolClassConverter.INSTANCE.auditClassDTOToSchoolClass(auditClassDTO));
    }

    @Override
    public List<SchoolClassVO> querySchoolClassList(List<AuditStatus> auditStatusList) {
        if(CollectionUtils.isEmpty(auditStatusList)) {
            return new ArrayList<>();
        }
        List<SchoolClass> schoolClassList = this.lambdaQuery()
                .in(SchoolClass::getAuditStatus, auditStatusList)
                .orderBy(Boolean.TRUE, Boolean.TRUE, SchoolClass::getAuditStatus)
                .list();
        return SchoolClassConverter.INSTANCE.schoolClassListToSchoolClassVOList(schoolClassList);
    }

    @Override
    public void attendSchoolClass(Long classId, Long userId) {
        redisLock.tryLockDoSomething(String.format(SchoolClassConstants.CLASS_TEACHER_ATTEND_LOCK, classId, userId), () -> {
            SchoolClass dbSchoolClass = checkAndGetSchoolClass(classId);
            // 审核未通过无法加入
            if(!AuditStatus.AUDIT_PASSED.equals(dbSchoolClass.getAuditStatus())) {
                throw new GlobalServiceException(GlobalServiceStatusCode.AUDIT_STATUS_NOT_APPROVED);
            }
            // 不必加入自己创建的班级
            if(dbSchoolClass.getCreatorId().equals(userId)) {
                throw new GlobalServiceException("不必加入自己创建的班级", GlobalServiceStatusCode.USER_NO_PERMISSION);
            }
            // 若未加入则加入
            classUserLinkService.getClassUserLink(classId, userId).ifPresentOrElse(ct -> {
                throw new GlobalServiceException(GlobalServiceStatusCode.SCHOOL_CLASS_USER_ATTENDED);
            }, () -> {
                classUserLinkService.createClassUserLink(classId, userId);
            });
        }, () -> {});
    }

    @Override
    public SchoolClassDetailVO querySchoolClassUserList(Long classId) {
        return schoolClassMapper.querySchoolClassUserList(classId);
    }

    @Override
    public List<SchoolClassAboutMeVO> querySchoolClassAboutMeList(Long userId) {
        return schoolClassMapper.querySchoolClassAboutMeList(userId);
    }

    @Override
    public void auditClassUser(Long classId, Long userId, AuditStatus auditStatus) {
        classUserLinkService.auditClassUser(classId, userId, auditStatus);
    }

    @Override
    public void checkSchoolClassApproved(Long classId) {
        if(!AuditStatus.AUDIT_PASSED.equals(checkAndGetSchoolClass(classId).getAuditStatus())) {
            throw new GlobalServiceException(GlobalServiceStatusCode.AUDIT_STATUS_NOT_APPROVED);
        }
    }

    @Override
    public void checkCreatorOfSchoolClass(Long classId, Long userId) {
        if(!isCreatorOfSchoolClass(classId, userId)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NOT_CLASS_CREATOR);
        }
    }

    @Override
    public boolean isCreatorOfSchoolClass(Long classId, Long userId) {
        return checkAndGetSchoolClass(classId).getCreatorId().equals(userId);
    }

    @Override
    public void checkPartnerOfSchoolClass(Long classId, Long userId) {
        if(!isPartnerOfSchoolClass(classId, userId)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NOT_CLASS_PARTNER);
        }
    }

    @Override
    public boolean isPartnerOfSchoolClass(Long classId, Long userId) {
        return classUserLinkService.getClassUserLink(classId, userId)
                .map(cu -> AuditStatus.AUDIT_PASSED.equals(cu.getAuditStatus()))
                .orElse(Boolean.FALSE) || isCreatorOfSchoolClass(classId, userId);

    }
}




