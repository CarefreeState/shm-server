package com.macaron.homeschool.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macaron.homeschool.common.exception.GlobalServiceException;

import java.io.IOException;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-01-21
 * Time: 12:17
 */
public class JsonUtil {

    public final static ObjectMapper OBJECT_MAPPER;

    static {
        // 忽略未定义的属性
        OBJECT_MAPPER = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
    }


    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

}
