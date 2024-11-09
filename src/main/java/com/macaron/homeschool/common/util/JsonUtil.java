package com.macaron.homeschool.common.util;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.macaron.homeschool.common.exception.GlobalServiceException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.macaron.homeschool.common.constants.DateTimeConstants.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-01-21
 * Time: 12:17
 */
public class JsonUtil {

    public final static ObjectMapper OBJECT_MAPPER;

    private final static String TYPE_KEY = "@type@";

    private final static String DEFAULT_TYPE = Object.class.getName();

    static {
        OBJECT_MAPPER = new Jackson2ObjectMapperBuilder()
                .indentOutput(Boolean.TRUE)
                .dateFormat(DATE_FORMAT)
                .simpleDateFormat(DATE_TIME_PATTERN)
                .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .modulesToInstall(new ParameterNamesModule())
                .build()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE)
        ;
    }

    public static <T> T analyzeJsonField(String json, String path, Class<T> clazz) {
        return JSONUtil.parse(json).getByPath(path, clazz);
    }

    public static <T> String addJsonField(String json, String key, T value) {
        JSON jsonObject = JSONUtil.parse(json);
        jsonObject.putByPath(key, value);
        return jsonObject.toString();
    }

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

    public static Object parse(String json) {
        try {
            String className = analyzeJsonField(json, TYPE_KEY, String.class);
            className = Optional.ofNullable(className).filter(StringUtils::hasText).orElse(DEFAULT_TYPE);
            return OBJECT_MAPPER.readValue(json, Class.forName(className));
        } catch (Exception e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

    public static String toJson(Object obj) {
        try {
            return addJsonField(OBJECT_MAPPER.writeValueAsString(obj), TYPE_KEY, obj.getClass().getName());
        } catch (JsonProcessingException e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

}
