package com.macaron.homeschool.controller;

import com.macaron.homeschool.common.SystemJsonResponse;
import com.macaron.homeschool.common.annotation.Intercept;
import com.macaron.homeschool.common.context.BaseContext;
import com.macaron.homeschool.common.enums.GlobalServiceStatusCode;
import com.macaron.homeschool.common.enums.UserType;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import com.macaron.homeschool.model.dto.ClassMessageDTO;
import com.macaron.homeschool.model.dto.ClassMessageQueryDTO;
import com.macaron.homeschool.model.vo.ClassMessageDetailVO;
import com.macaron.homeschool.model.vo.ClassMessageQueryVO;
import com.macaron.homeschool.service.ClassMessageService;
import com.macaron.homeschool.service.SchoolClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-07
 * Time: 0:39
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "班级通知测试接口")
@RequestMapping("/api/v1/message/class")
@Intercept(permit = {UserType.TEACHER})
public class ClassMessageController {

    private final ClassMessageService classMessageService;

    private final SchoolClassService schoolClassService;

    @PostMapping("/release")
    @Operation(summary = "发布班级通知")
    public SystemJsonResponse<Long> releaseClassMessage(@Valid @RequestBody ClassMessageDTO classMessageDTO) {
        // 这里必然是老师
        Long teacherId = BaseContext.getCurrentUser().getUserId();
        Long classId = classMessageDTO.getClassId();
        // 判断班级是否有效
        schoolClassService.checkSchoolClassApproved(classId);
        // 判断是否是班级的老师
        schoolClassService.checkPartnerOfSchoolClass(classId, teacherId);
        // 发布
        Long messageId = classMessageService.releaseClassMessage(teacherId, classMessageDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(messageId);
    }

    @DeleteMapping("/remove/{messageId}")
    @Operation(summary = "删除班级通知")
    public SystemJsonResponse<?> removeClassMessage(@PathVariable("messageId") @NotNull(message = "班级通知 id 不能为空") Long messageId) {
        Long creatorId = classMessageService.checkAndGetClassMessage(messageId).getCreatorId();
        if(!BaseContext.getCurrentUser().getUserId().equals(creatorId)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }
        classMessageService.removeClassMessage(messageId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/query")
    @Operation(summary = "条件分页查询班级通知列表")
    @Intercept(permit = {UserType.TEACHER, UserType.GUARDIAN})
    public SystemJsonResponse<ClassMessageQueryVO> queryClassMessageList(@Valid @RequestBody ClassMessageQueryDTO classMessageQueryDTO) {
        Long userId = BaseContext.getCurrentUser().getUserId();
        ClassMessageQueryVO classMessageQueryVO = classMessageService.queryClassMessageList(userId, classMessageQueryDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(classMessageQueryVO);
    }

    @GetMapping("/detail/{messageId}")
    @Operation(summary = "查看班级通知详情")
    @Intercept(permit = {UserType.TEACHER, UserType.GUARDIAN})
    public SystemJsonResponse<ClassMessageDetailVO> queryClassMessageDetail(@PathVariable("messageId") @NotNull(message = "系统消息 id 不能为空") Long messageId) {
        Long userId = BaseContext.getCurrentUser().getUserId();
        Long classId = classMessageService.checkAndGetClassMessage(messageId).getClassId();
        // 判断是否在班级中
        schoolClassService.checkPartnerOfSchoolClass(classId, userId);
        ClassMessageDetailVO classMessageDetailVO = classMessageService.queryClassMessageDetail(messageId, userId);
        return SystemJsonResponse.SYSTEM_SUCCESS(classMessageDetailVO);
    }
}
