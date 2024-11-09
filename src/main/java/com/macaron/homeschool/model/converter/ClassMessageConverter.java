package com.macaron.homeschool.model.converter;

import com.macaron.homeschool.common.base.BasePageQuery;
import com.macaron.homeschool.common.base.BasePageResult;
import com.macaron.homeschool.model.dto.ClassMessageDTO;
import com.macaron.homeschool.model.dto.ClassMessageQueryDTO;
import com.macaron.homeschool.model.entity.ClassMessage;
import com.macaron.homeschool.model.vo.ClassMessageDetailVO;
import com.macaron.homeschool.model.vo.ClassMessageQueryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-06
 * Time: 22:30
 */
@Mapper
public interface ClassMessageConverter {

    ClassMessageConverter INSTANCE = Mappers.getMapper(ClassMessageConverter.class);

    ClassMessage classMessageDTOToClassMessage(ClassMessageDTO classMessageDTO);

    ClassMessageDetailVO classMessageToClassMessageDetailVO(ClassMessage classMessage);

    BasePageQuery classMessageQueryDTOToBasePageQuery(ClassMessageQueryDTO classMessageQueryDTO);

    ClassMessageQueryVO basePageResultToClassMessageQueryVO(BasePageResult<ClassMessage> basePageResult);
}
