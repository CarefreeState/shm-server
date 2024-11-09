package com.macaron.homeschool.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.macaron.homeschool.common.base.BasePageQuery;
import com.macaron.homeschool.common.base.BasePageResult;
import com.macaron.homeschool.common.enums.GlobalServiceStatusCode;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import com.macaron.homeschool.model.converter.SystemMessageConverter;
import com.macaron.homeschool.model.dao.mapper.SystemMessageMapper;
import com.macaron.homeschool.model.dto.SystemMessageDTO;
import com.macaron.homeschool.model.dto.SystemMessageQueryDTO;
import com.macaron.homeschool.model.entity.SystemMessage;
import com.macaron.homeschool.model.vo.SystemMessageDetailVO;
import com.macaron.homeschool.model.vo.SystemMessageQueryVO;
import com.macaron.homeschool.service.SystemMessageService;
import com.macaron.homeschool.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【system_message(系统消息表)】的数据库操作Service实现
* @createDate 2024-11-05 21:06:05
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class SystemMessageServiceImpl extends ServiceImpl<SystemMessageMapper, SystemMessage>
    implements SystemMessageService{

    private final UserService userService;

    @Override
    public Optional<SystemMessage> getSystemMessage(Long messageId) {
        return this.lambdaQuery()
                .eq(SystemMessage::getId, messageId)
                .oneOpt();
    }

    @Override
    public Long releaseSystemMessage(Long managerId, SystemMessageDTO systemMessageDTO) {
        SystemMessage systemMessage = SystemMessageConverter.INSTANCE.systemMessageDTOToSystemMessage(systemMessageDTO);
        systemMessage.setCreatorId(managerId);
        this.save(systemMessage);
        log.info("管理员 {} 发布系统消息 {}", managerId, systemMessage);
        return systemMessage.getId();
    }

    @Override
    public void removeSystemMessage(Long messageId) {
        this.lambdaUpdate()
                .eq(SystemMessage::getId, messageId)
                .remove();
    }

    @Override
    public SystemMessageQueryVO querySystemMessageList(Long userId, SystemMessageQueryDTO systemMessageQueryDTO) {
        // 解析分页参数获取 page
        IPage<SystemMessage> page = null;
        if(Objects.isNull(systemMessageQueryDTO)) {
            page = new BasePageQuery().toMpPage();
        } else {
            page = SystemMessageConverter.INSTANCE.systemMessageQueryDTOToBasePageQuery(systemMessageQueryDTO).toMpPage();
        }
        // 分页
        IPage<SystemMessage> systemMessageIPage = this.lambdaQuery()
                .eq(Boolean.TRUE.equals(systemMessageQueryDTO.getIsFromMe()), SystemMessage::getCreatorId, userId)
                .page(page);
        // 封装
        BasePageResult<SystemMessage> pageResult = BasePageResult.of(systemMessageIPage);
        // 转化
        return SystemMessageConverter.INSTANCE.basePageResultToSystemMessageQueryVO(pageResult);
    }

    @Override
    public SystemMessage checkAndGetSystemMessage(Long messageId) {
        return getSystemMessage(messageId).orElseThrow(() ->
                new GlobalServiceException(GlobalServiceStatusCode.SYSTEM_MESSAGE_NOT_EXISTS));
    }

    @Override
    public SystemMessageDetailVO querySystemMessageDetail(Long messageId) {
        SystemMessage systemMessage = checkAndGetSystemMessage(messageId);
        SystemMessageDetailVO systemMessageDetailVO = SystemMessageConverter.INSTANCE.systemMessageToSystemMessageDetailVO(systemMessage);
        systemMessageDetailVO.setUserInfoVO(userService.getUserInfoVOById(systemMessage.getCreatorId()));
        return systemMessageDetailVO;
    }
}




