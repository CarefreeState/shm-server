package com.macaron.homeschool.model.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.macaron.homeschool.model.entity.SchoolClass;
import com.macaron.homeschool.model.vo.SchoolClassAboutMeVO;
import com.macaron.homeschool.model.vo.SchoolClassDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【school_class(班级表)】的数据库操作Mapper
* @createDate 2024-11-05 21:06:05
* @Entity com.macaron.homeschool.model.entity.SchoolClass
*/
public interface SchoolClassMapper extends BaseMapper<SchoolClass> {

    SchoolClassDetailVO querySchoolClassUserList(@Param("classId") Long classId);

    List<SchoolClassAboutMeVO> querySchoolClassAboutMeList(@Param("userId") Long userId);
}




