package com.macaron.homeschool.controller;

import com.macaron.homeschool.common.SystemJsonResponse;
import com.macaron.homeschool.common.annotation.Intercept;
import com.macaron.homeschool.common.context.BaseContext;
import com.macaron.homeschool.common.enums.AuditStatus;
import com.macaron.homeschool.common.enums.GlobalServiceStatusCode;
import com.macaron.homeschool.common.enums.UserType;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import com.macaron.homeschool.common.util.HttpServletUtil;
import com.macaron.homeschool.model.dto.AuditClassDTO;
import com.macaron.homeschool.model.dto.AuditClassUserDTO;
import com.macaron.homeschool.model.dto.SchoolClassDTO;
import com.macaron.homeschool.model.vo.SchoolClassAboutMeVO;
import com.macaron.homeschool.model.vo.SchoolClassDetailVO;
import com.macaron.homeschool.model.vo.SchoolClassVO;
import com.macaron.homeschool.service.SchoolClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 20:52
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "班级测试接口")
@RequestMapping("/api/v1/class")
public class SchoolClassController {

    private final SchoolClassService schoolClassService;

    @PostMapping("/create")
    @Operation(summary = "创建一个新班级")
    @Intercept(permit = {UserType.TEACHER})
    public SystemJsonResponse<Long> createSchoolClass(@Valid @RequestBody SchoolClassDTO schoolClassDTO) {
        Long teacherId = BaseContext.getCurrentUser().getUserId();
        Long classId = schoolClassService.createSchoolClass(teacherId, schoolClassDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS(classId);
    }

    @PutMapping("/update/{classId}")
    @Operation(summary = "更新班级信息")
    @Intercept(permit = {UserType.TEACHER})
    public SystemJsonResponse<?> updateSchoolClass(@PathVariable("classId") @NotNull(message = "班级 id 不能为空") Long classId,
                                                   @Valid @RequestBody SchoolClassDTO schoolClassDTO) {
        Long teacherId = BaseContext.getCurrentUser().getUserId();
        schoolClassService.updateSchoolClass(classId, teacherId, schoolClassDTO);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/query/all")
    @Operation(summary = "管理员查询全部班级列表")
    @Intercept(permit = {UserType.MANAGER})
    public SystemJsonResponse<List<SchoolClassVO>> managerQueryClassList() {
        List<SchoolClassVO> schoolClassVOList = schoolClassService.querySchoolClassList(List.of(AuditStatus.values()));
        return SystemJsonResponse.SYSTEM_SUCCESS(schoolClassVOList);
    }

    @PutMapping("/audit/class")
    @Operation(summary = "管理员审核班级")
    @Intercept(permit = {UserType.MANAGER})
    public SystemJsonResponse<?> auditSchoolClass(@Valid @RequestBody AuditClassDTO auditClassDTO) {
        schoolClassService.auditSchoolClass(auditClassDTO.getClassId(), AuditStatus.get(auditClassDTO.getAuditStatus()));
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/query/normal")
    @Operation(summary = "查询审核通过的班级列表")
    @Intercept(permit = {UserType.MANAGER, UserType.TEACHER, UserType.GUARDIAN})
    public SystemJsonResponse<List<SchoolClassVO>> queryClassList() {
        List<SchoolClassVO> schoolClassVOList = schoolClassService.querySchoolClassList(List.of(AuditStatus.AUDIT_PASSED));
        return SystemJsonResponse.SYSTEM_SUCCESS(schoolClassVOList);
    }

    @GetMapping("/query/self")
    @Operation(summary = "查询自己有关的班级列表")
    @Intercept(permit = {UserType.TEACHER, UserType.GUARDIAN})
    public SystemJsonResponse<List<SchoolClassAboutMeVO>> querySelfSchoolClassList() {
        Long userId = BaseContext.getCurrentUser().getUserId();
        List<SchoolClassAboutMeVO> schoolClassAboutMeVOList = schoolClassService.querySchoolClassAboutMeList(userId);
        return SystemJsonResponse.SYSTEM_SUCCESS(schoolClassAboutMeVOList);
    }

    @PutMapping("/attend/{classId}")
    @Operation(summary = "老师/家长加入班级")
    @Intercept(permit = {UserType.TEACHER, UserType.GUARDIAN})
    public SystemJsonResponse<?> attendSchoolClass(@PathVariable("classId") @NotNull(message = "班级 id 不能为空") Long classId) {
        Long userId = BaseContext.getCurrentUser().getUserId();
        schoolClassService.attendSchoolClass(classId, userId);
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @PutMapping("/audit/user")
    @Operation(summary = "老师审核加入班级的用户")
    @Intercept(permit = {UserType.TEACHER})
    public SystemJsonResponse<?> auditClassUser(@Valid @RequestBody AuditClassUserDTO auditClassUserDTO) {
        Long teacherId = BaseContext.getCurrentUser().getUserId();
        Long userId = auditClassUserDTO.getUserId();
        if(teacherId.equals(userId)) {
            throw new GlobalServiceException(GlobalServiceStatusCode.USER_NO_PERMISSION);
        }
        // 判断是不是创建者
        Long classId = auditClassUserDTO.getClassId();
        schoolClassService.checkCreatorOfSchoolClass(classId, teacherId);
        schoolClassService.auditClassUser(classId, userId, AuditStatus.get(auditClassUserDTO.getAuditStatus()));
        return SystemJsonResponse.SYSTEM_SUCCESS();
    }

    @GetMapping("/query/users/{classId}")
    @Operation(summary = "查询班级内用户列表")
    @Intercept(permit = {UserType.TEACHER, UserType.GUARDIAN})
    public SystemJsonResponse<SchoolClassDetailVO> querySchoolClassUserList(@PathVariable("classId") @NotNull(message = "班级 id 不能为空") Long classId,
                                                                            HttpServletResponse httpServletResponse) {
        SchoolClassDetailVO schoolClassDetailVO = schoolClassService.querySchoolClassUserList(classId);
        // 不是一员的话不能看到其他成员
        if(!schoolClassService.isPartnerOfSchoolClass(classId, BaseContext.getCurrentUser().getUserId())) {
            HttpServletUtil.warn(httpServletResponse, "当前用户还未成为班级中的一员");
            schoolClassDetailVO.setUserList(new ArrayList<>());
        }
        return SystemJsonResponse.SYSTEM_SUCCESS(schoolClassDetailVO);
    }

}
