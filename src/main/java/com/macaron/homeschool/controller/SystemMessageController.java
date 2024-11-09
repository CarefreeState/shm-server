package com.macaron.homeschool.controller;

import com.macaron.homeschool.common.SystemJsonResponse;
import com.macaron.homeschool.common.annotation.Intercept;
import com.macaron.homeschool.common.context.BaseContext;
import com.macaron.homeschool.common.enums.GlobalServiceStatusCode;
import com.macaron.homeschool.common.enums.UserType;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import com.macaron.homeschool.model.dto.SystemMessageDTO;
import com.macaron.homeschool.model.dto.SystemMessageQueryDTO;
import com.macaron.homeschool.model.vo.SystemMessageDetailVO;
import com.macaron.homeschool.model.vo.SystemMessageQueryVO;
import com.macaron.homeschool.service.SystemMessageService;
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
 * Date: 2024-11-06
 * Time: 15:30
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "系统消息测试接口")
@RequestMapping("/api/v1/message/system")
@Intercept(permit = {UserType.MANAGER})
public class SystemMessageController {

    private final SystemMessageService systemMessageService;

    @PostMapping("/release")
    @Operation(summary = "发布系统消息")
    public SystemJsonResponse<Long> releaseSystemMessage(@Valid @RequestBody SystemMessageDTO systemMessageDTO) {
        Long managerId = BaseContext.getCurrentUser().getUserId();
        Long messageId = systemMessageService.releaseSystemMessage(managerId, systemMessageDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(messageId);
    }

    @DeleteMapping("/remove/{messageId}")
    @Operation(summary = "删除系统消息")
    public SystemJsonResponse<?> removeSystemMessage(@PathVariable("messageId") @NotNull(message = "系统消息 id 不能为空") Long messageId) {
        Long creatorId = systemMessageService.checkAndGetSystemMessage(messageId).getCreatorId();
        if(!BaseContext.getCurrentUser().getUserId().equals(creatorId)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }
        systemMessageService.removeSystemMessage(messageId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PostMapping("/query")
    @Operation(summary = "条件分页查询系统消息列表")
    @Intercept(permit = {UserType.MANAGER, UserType.TEACHER, UserType.GUARDIAN})
    public SystemJsonResponse<SystemMessageQueryVO> querySystemMessageList(@Valid @RequestBody(required = false) SystemMessageQueryDTO systemMessageQueryDTO) {
        Long userId = BaseContext.getCurrentUser().getUserId();
        SystemMessageQueryVO systemMessageQueryVO = systemMessageService.querySystemMessageList(userId, systemMessageQueryDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(systemMessageQueryVO);
    }

    @GetMapping("/detail/{messageId}")
    @Operation(summary = "查看系统消息详情")
    @Intercept(permit = {UserType.MANAGER, UserType.TEACHER, UserType.GUARDIAN})
    public SystemJsonResponse<SystemMessageDetailVO> querySystemMessageDetail(@PathVariable("messageId") @NotNull(message = "系统消息 id 不能为空") Long messageId) {
        SystemMessageDetailVO systemMessageDetailVO = systemMessageService.querySystemMessageDetail(messageId);
        return SystemJsonResponse.SYSTEM_SUCCESS(systemMessageDetailVO);
    }

}
