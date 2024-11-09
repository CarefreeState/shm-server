package com.macaron.homeschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macaron.homeschool.model.dto.SystemMessageDTO;
import com.macaron.homeschool.model.dto.SystemMessageQueryDTO;
import com.macaron.homeschool.model.entity.SystemMessage;
import com.macaron.homeschool.model.vo.SystemMessageDetailVO;
import com.macaron.homeschool.model.vo.SystemMessageQueryVO;

import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【system_message(系统消息表)】的数据库操作Service
* @createDate 2024-11-05 21:06:05
*/
public interface SystemMessageService extends IService<SystemMessage> {

    Optional<SystemMessage> getSystemMessage(Long messageId);

    Long releaseSystemMessage(Long managerId, SystemMessageDTO systemMessageDTO);

    void removeSystemMessage(Long messageId);

    SystemMessageQueryVO querySystemMessageList(Long userId, SystemMessageQueryDTO systemMessageQueryDTO);

    SystemMessage checkAndGetSystemMessage(Long messageId);

    SystemMessageDetailVO querySystemMessageDetail(Long messageId);

}
