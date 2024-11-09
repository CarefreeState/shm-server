package com.macaron.homeschool.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macaron.homeschool.common.base.BasePageQuery;
import com.macaron.homeschool.common.base.BasePageResult;
import com.macaron.homeschool.common.constants.UserConstants;
import com.macaron.homeschool.common.enums.GlobalServiceStatusCode;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import com.macaron.homeschool.model.converter.SiteMessageConverter;
import com.macaron.homeschool.model.dao.mapper.SiteMessageMapper;
import com.macaron.homeschool.model.dto.SiteMessageDTO;
import com.macaron.homeschool.model.dto.SiteMessageQueryDTO;
import com.macaron.homeschool.model.dto.SiteOppositeQueryDTO;
import com.macaron.homeschool.model.entity.SiteMessage;
import com.macaron.homeschool.model.vo.SiteMessageQueryVO;
import com.macaron.homeschool.model.vo.SiteMessageVO;
import com.macaron.homeschool.model.vo.UserVO;
import com.macaron.homeschool.service.SchoolClassService;
import com.macaron.homeschool.service.SiteMessageService;
import com.macaron.homeschool.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【site_message(站内信表)】的数据库操作Service实现
* @createDate 2024-11-05 21:06:05
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class SiteMessageServiceImpl extends ServiceImpl<SiteMessageMapper, SiteMessage>
    implements SiteMessageService{

    private final SiteMessageMapper siteMessageMapper;

    private final SchoolClassService schoolClassService;
    
    private final UserService userService;

    @Override
    public Optional<SiteMessage> getSiteMessage(Long messageId) {
        return this.lambdaQuery()
                .eq(SiteMessage::getId, messageId)
                .oneOpt();
    }

    @Override
    public Long releaseSiteMessage(Long userId, SiteMessageDTO siteMessageDTO) {
        SiteMessage siteMessage = SiteMessageConverter.INSTANCE.siteMessageDTOToSiteMessage(siteMessageDTO);
        siteMessage.setSenderId(userId);
        this.save(siteMessage);
        log.info("用户 {} 发送站内信 {}", userId, siteMessage);
        return siteMessage.getId();
    }

    @Override
    public void removeSiteMessage(Long messageId) {
        this.lambdaUpdate()
                .eq(SiteMessage::getId, messageId)
                .remove();
    }

    @Override
    public SiteMessageQueryVO querySiteMessageList(Long userId, SiteMessageQueryDTO siteMessageQueryDTO) {
        // 解析分页参数获取 page
        IPage<SiteMessageVO> page = null;
        Long classId = null;
        Long oppositeId = null;
        Boolean isFromMe = null;
        if(Objects.isNull(siteMessageQueryDTO)) {
            page = new BasePageQuery().toMpPage();
        } else {
            page = SiteMessageConverter.INSTANCE.siteMessageQueryDTOToBasePageQuery(siteMessageQueryDTO).toMpPage();
            classId = siteMessageQueryDTO.getClassId();
            oppositeId = siteMessageQueryDTO.getOppositeId();
            isFromMe = siteMessageQueryDTO.getIsFromMe();
        }
        // 如果 classId 不为 null，userId 必须是该班级的
        if(Objects.nonNull(classId)) {
            schoolClassService.checkSchoolClassApproved(classId);
            schoolClassService.checkPartnerOfSchoolClass(classId, userId);
        }
        // 分页
        IPage<SiteMessageVO> siteMessageVOIPage = siteMessageMapper.querySiteMessageList(page, userId, classId, oppositeId, isFromMe);
        // 封装
        BasePageResult<SiteMessageVO> pageResult = BasePageResult.of(siteMessageVOIPage);
        // 转化
        SiteMessageQueryVO siteMessageQueryVO = SiteMessageConverter.INSTANCE.basePageResultToSiteMessageQueryVO(pageResult);
        siteMessageQueryVO.getList().forEach(message -> {
            UserVO sender = message.getSender();
            if(userId.equals(sender.getId())) {
               sender.setNickname(UserConstants.MYSELF);
           }
            UserVO recipient = message.getRecipient();
            if(userId.equals(recipient.getId())) {
                recipient.setNickname(UserConstants.MYSELF);
            }
        });
        return siteMessageQueryVO;
    }

    @Override
    public List<UserVO> queryOppositeList(Long userId, SiteOppositeQueryDTO siteOppositeQueryDTO) {
        Long classId = Optional.ofNullable(siteOppositeQueryDTO).map(SiteOppositeQueryDTO::getClassId).orElse(null);
        // 如果 classId 不为 null，userId 必须是该班级的
        if(Objects.nonNull(classId)) {
            schoolClassService.checkSchoolClassApproved(classId);
            schoolClassService.checkPartnerOfSchoolClass(classId, userId);
        }
        return siteMessageMapper.querySiteMessageListByUserId(userId, classId)
                .stream()
                .map(m -> m.getSender().getId().equals(userId) ? m.getRecipient() : m.getSender())
                .distinct()
                .toList();
    }

    @Override
    public SiteMessage checkAndGetSiteMessage(Long messageId) {
        return getSiteMessage(messageId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.SITE_MESSAGE_NOT_EXISTS));
    }

    @Override
    public SiteMessageVO querySiteMessageDetail(Long userId, Long messageId) {
        SiteMessage siteMessage = checkAndGetSiteMessage(messageId);
        Long senderId = siteMessage.getSenderId();
        Long recipientId = siteMessage.getRecipientId();
        // 只有双方中的一员能看到
        if(!userId.equals(senderId) && !userId.equals(recipientId)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }
        SiteMessageVO siteMessageVO = SiteMessageConverter.INSTANCE.siteMessageToSiteMessageVO(siteMessage);
        UserVO sender = userService.getUserVOById(senderId);
        UserVO recipient = userService.getUserVOById(recipientId);
        siteMessageVO.setSender(sender);
        siteMessageVO.setRecipient(recipient);
        return siteMessageVO;
    }
}




