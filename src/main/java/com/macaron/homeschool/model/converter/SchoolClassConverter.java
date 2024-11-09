package com.macaron.homeschool.model.converter;

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

    SchoolClass schoolClassDTOToSchoolClass(SchoolClassDTO schoolClassDTO);

    @Mapping(target = "auditStatus", expression = "java(schoolClass.getAuditStatus().getCode())")
    SchoolClassVO schoolClassToSchoolClassVO(SchoolClass schoolClass);

    List<SchoolClassVO> schoolClassListToSchoolClassVOList(List<SchoolClass> schoolClassList);

}
