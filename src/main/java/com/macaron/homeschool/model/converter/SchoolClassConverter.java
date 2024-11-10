package com.macaron.homeschool.model.converter;

import com.macaron.homeschool.model.dto.AuditClassDTO;
import com.macaron.homeschool.model.dto.SchoolClassDTO;
import com.macaron.homeschool.model.entity.SchoolClass;
import com.macaron.homeschool.model.vo.SchoolClassVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 16:26
 */
@Mapper
public interface SchoolClassConverter {

    SchoolClassConverter INSTANCE = Mappers.getMapper(SchoolClassConverter.class);

    @Mapping(target = "id", source = "classId")
    @Mapping(target = "auditStatus", expression = "java(com.macaron.homeschool.common.enums.AuditStatus.get(auditClassDTO.getAuditStatus()))")
    SchoolClass auditClassDTOToSchoolClass(AuditClassDTO auditClassDTO);

    SchoolClass schoolClassDTOToSchoolClass(SchoolClassDTO schoolClassDTO);

    @Mapping(target = "auditStatus", expression = "java(java.util.Optional.ofNullable(schoolClass.getAuditStatus()).map(com.macaron.homeschool.common.enums.AuditStatus::getCode).orElse(null))")
    SchoolClassVO schoolClassToSchoolClassVO(SchoolClass schoolClass);

    List<SchoolClassVO> schoolClassListToSchoolClassVOList(List<SchoolClass> schoolClassList);

}
