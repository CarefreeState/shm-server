package com.macaron.homeschool.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macaron.homeschool.common.base.BasePageResult;
import com.macaron.homeschool.common.enums.GlobalServiceStatusCode;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import com.macaron.homeschool.model.converter.ClassMessageConverter;
import com.macaron.homeschool.model.dao.mapper.ClassMessageMapper;
import com.macaron.homeschool.model.dto.ClassMessageDTO;
import com.macaron.homeschool.model.dto.ClassMessageQueryDTO;
import com.macaron.homeschool.model.entity.ClassMessage;
import com.macaron.homeschool.model.vo.ClassMessageDetailVO;
import com.macaron.homeschool.model.vo.ClassMessageQueryVO;
import com.macaron.homeschool.service.ClassMessageService;
import com.macaron.homeschool.service.SchoolClassService;
import com.macaron.homeschool.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【class_message(班级通知表)】的数据库操作Service实现
* @createDate 2024-11-05 21:06:05
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class ClassMessageServiceImpl extends ServiceImpl<ClassMessageMapper, ClassMessage>
    implements ClassMessageService{

    private final UserService userService;

    private final SchoolClassService schoolClassService;

    @Override
    public Optional<ClassMessage> getClassMessage(Long messageId) {
        return this.lambdaQuery()
                .eq(ClassMessage::getId, messageId)
                .oneOpt();
    }

    @Override
    public Long releaseClassMessage(Long teacherId, ClassMessageDTO classMessageDTO) {
        ClassMessage classMessage = ClassMessageConverter.INSTANCE.classMessageDTOToClassMessage(classMessageDTO);
        classMessage.setCreatorId(teacherId);
        this.save(classMessage);
        log.info("老师 {} 发布班级通知 {}", teacherId, classMessage);
        return classMessage.getId();
    }

    @Override
    public void removeClassMessage(Long messageId) {
        this.lambdaUpdate()
                .eq(ClassMessage::getId, messageId)
                .remove();
    }

    @Override
    public ClassMessageQueryVO queryClassMessageList(Long userId, ClassMessageQueryDTO classMessageQueryDTO) {
        // 解析分页参数获取 page
        IPage<ClassMessage> page = ClassMessageConverter.INSTANCE.classMessageQueryDTOToBasePageQuery(classMessageQueryDTO).toMpPage();
        Long classId = classMessageQueryDTO.getClassId();
        // 用户必须是该班级的
        schoolClassService.checkSchoolClassApproved(classId);
        schoolClassService.checkPartnerOfSchoolClass(classId, userId);
        // 分页
        IPage<ClassMessage> classMessageIPage = this.lambdaQuery()
                .eq(Objects.nonNull(classId), ClassMessage::getClassId, classId)
                .eq(Boolean.TRUE.equals(classMessageQueryDTO.getIsFromMe()), ClassMessage::getCreatorId, userId)
                .page(page);
        // 封装
        BasePageResult<ClassMessage> pageResult = BasePageResult.of(classMessageIPage);
        // 转化
        return ClassMessageConverter.INSTANCE.basePageResultToClassMessageQueryVO(pageResult);
    }

    @Override
    public ClassMessage checkAndGetClassMessage(Long messageId) {
        return getClassMessage(messageId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.CLASS_MESSAGE_NOT_EXISTS));
    }

    @Override
    public ClassMessageDetailVO queryClassMessageDetail(Long messageId, Long userId) {
        ClassMessage classMessage = checkAndGetClassMessage(messageId);
        ClassMessageDetailVO systemMessageDetailVO = ClassMessageConverter.INSTANCE.classMessageToClassMessageDetailVO(classMessage);
        systemMessageDetailVO.setUserInfoVO(userService.getUserInfoVOById(classMessage.getCreatorId()));
        return systemMessageDetailVO;
    }
}




