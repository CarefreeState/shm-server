package com.macaron.homeschool.model.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.macaron.homeschool.model.entity.SiteMessage;
import com.macaron.homeschool.model.vo.SiteMessageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【site_message(站内信表)】的数据库操作Mapper
* @createDate 2024-11-05 21:06:05
* @Entity com.macaron.homeschool.model.entity.SiteMessage
*/
public interface SiteMessageMapper extends BaseMapper<SiteMessage> {

    IPage<SiteMessageVO> querySiteMessageList(IPage<SiteMessageVO> page, @Param("userId") Long userId, @Param("classId") Long classId, @Param("oppositeId") Long oppositeId, @Param("isFromMe") Boolean isFromMe);

    List<SiteMessageVO> querySiteMessageListByUserId(@Param("userId") Long userId, @Param("classId") Long classId);
}




