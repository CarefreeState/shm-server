package com.macaron.homeschool.redis.cache;

import com.macaron.homeschool.common.util.JsonUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class RedisObjectSerializer implements RedisSerializer<Object> {
 
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
 
    @Override
    public byte[] serialize(Object object) throws SerializationException {
        return Optional.ofNullable(object)
                .map(data -> JsonUtil.toJson(data).getBytes(DEFAULT_CHARSET))
                .orElseGet(() -> new byte[0]);
    }
 
    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return Optional.ofNullable(bytes)
                .filter(data -> data.length > 0)
                .map(data -> JsonUtil.parse(new String(bytes, DEFAULT_CHARSET)))
                .orElse(null);
    }

}