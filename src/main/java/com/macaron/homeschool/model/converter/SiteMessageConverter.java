package com.macaron.homeschool.model.converter;

import com.macaron.homeschool.common.base.BasePageQuery;
import com.macaron.homeschool.common.base.BasePageResult;
import com.macaron.homeschool.model.dto.SiteMessageDTO;
import com.macaron.homeschool.model.dto.SiteMessageQueryDTO;
import com.macaron.homeschool.model.entity.SiteMessage;
import com.macaron.homeschool.model.vo.SiteMessageQueryVO;
import com.macaron.homeschool.model.vo.SiteMessageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 23:38
 */
@Mapper
public interface SiteMessageConverter {

    SiteMessageConverter INSTANCE = Mappers.getMapper(SiteMessageConverter.class);

    SiteMessage siteMessageDTOToSiteMessage(SiteMessageDTO siteMessageDTO);

    SiteMessageVO siteMessageToSiteMessageVO(SiteMessage siteMessage);

    BasePageQuery siteMessageQueryDTOToBasePageQuery(SiteMessageQueryDTO siteMessageQueryDTO);

    SiteMessageQueryVO basePageResultToSiteMessageQueryVO(BasePageResult<SiteMessageVO> basePageResult);

}
