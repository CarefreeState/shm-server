package com.macaron.homeschool.model.converter;

import com.macaron.homeschool.common.base.BasePageQuery;
import com.macaron.homeschool.common.base.BasePageResult;
import com.macaron.homeschool.model.dto.SystemMessageDTO;
import com.macaron.homeschool.model.dto.SystemMessageQueryDTO;
import com.macaron.homeschool.model.entity.SystemMessage;
import com.macaron.homeschool.model.vo.SystemMessageDetailVO;
import com.macaron.homeschool.model.vo.SystemMessageQueryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 15:38
 */
@Mapper
public interface SystemMessageConverter {

    SystemMessageConverter INSTANCE = Mappers.getMapper(SystemMessageConverter.class);

    SystemMessage systemMessageDTOToSystemMessage(SystemMessageDTO systemMessage);

    SystemMessageDetailVO systemMessageToSystemMessageDetailVO(SystemMessage systemMessage);

    BasePageQuery systemMessageQueryDTOToBasePageQuery(SystemMessageQueryDTO systemMessageQueryDTO);

    SystemMessageQueryVO basePageResultToSystemMessageQueryVO(BasePageResult<SystemMessage> basePageResult);
}
