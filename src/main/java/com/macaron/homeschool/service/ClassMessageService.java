package com.macaron.homeschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.macaron.homeschool.model.dto.ClassMessageDTO;
import com.macaron.homeschool.model.dto.ClassMessageQueryDTO;
import com.macaron.homeschool.model.entity.ClassMessage;
import com.macaron.homeschool.model.vo.ClassMessageDetailVO;
import com.macaron.homeschool.model.vo.ClassMessageQueryVO;

import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【class_message(班级通知表)】的数据库操作Service
* @createDate 2024-11-05 21:06:05
*/
public interface ClassMessageService extends IService<ClassMessage> {

    Optional<ClassMessage> getClassMessage(Long messageId);

    Long releaseClassMessage(Long teacherId, ClassMessageDTO classMessageDTO);

    void removeClassMessage(Long messageId);

    // 条件分页查询
    ClassMessageQueryVO queryClassMessageList(Long userId, ClassMessageQueryDTO classMessageQueryDTO);

    ClassMessage checkAndGetClassMessage(Long messageId);

    ClassMessageDetailVO queryClassMessageDetail(Long messageId, Long userId);

}
