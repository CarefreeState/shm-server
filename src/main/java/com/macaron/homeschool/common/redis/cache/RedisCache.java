package com.macaron.homeschool.common.redis.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisCache {

    private final RedisTemplate<String, String> redisTemplate;

    private final RedisCacheSerializer redisCacheSerializer;

    public <K> Boolean expire(final K key, final long timeout, final TimeUnit timeUnit) {
        String jsonKey = redisCacheSerializer.toJson(key);
        log.info("为 Redis 的键值设置超时时间\t[{}]-[{}  {}]", jsonKey, timeout, timeUnit.name());
        return redisTemplate.expire(jsonKey, timeout, timeUnit);
    }

    public <K> long getKeyTTL(final K key, final TimeUnit timeUnit) {
        String jsonKey = redisCacheSerializer.toJson(key);
        int ttl = redisTemplate.opsForValue().getOperations().getExpire(jsonKey).intValue();
        String message = switch (ttl) {
            case -1 -> "没有设置过期时间";
            case -2 -> "key 不存在";
            default -> ttl + "  " + TimeUnit.SECONDS.name();
        };
        log.info("查询 Redis key[{}] 剩余存活时间:{}", jsonKey, message);
        return timeUnit.convert(ttl, TimeUnit.SECONDS);
    }

    public <K, V> void setObject(final K key, final V value) {
        String jsonKey = redisCacheSerializer.toJson(key);
        String jsonValue = redisCacheSerializer.toJson(value);
        log.info("存入 Redis\t[{}]-[{}]", jsonKey, jsonValue);
        redisTemplate.opsForValue().set(jsonKey, jsonValue);
    }

    public <K, V> void setObject(final K key, final V value, final long timout, final TimeUnit timeUnit) {
        String jsonKey = redisCacheSerializer.toJson(key);
        String jsonValue = redisCacheSerializer.toJson(value);
        log.info("存入 Redis\t[{}]-[{}]，超时时间:[{}  {}]", jsonKey, jsonValue, timout, timeUnit.name());
        redisTemplate.opsForValue().set(jsonKey, jsonValue, timout, timeUnit);
    }

    public <K, V> Optional<V> getObject(final K key, final Class<V> vClazz) {
        String jsonKey = redisCacheSerializer.toJson(key);
        String jsonValue = redisTemplate.opsForValue().get(jsonKey);
        log.info("查询 Redis\t[{}]-[{}]", jsonKey, jsonValue);
        return Optional.ofNullable(redisCacheSerializer.parse(jsonValue, vClazz));
    }

    public <K> Boolean deleteObject(final K key) {
        String jsonKey = redisCacheSerializer.toJson(key);
        log.info("删除 Redis 的键值\tkey[{}]", jsonKey);
        return redisTemplate.delete(jsonKey);
    }

    public <K> Long decrement(final K key) {
        String jsonKey = redisCacheSerializer.toJson(key);
        Long number = redisTemplate.opsForValue().decrement(jsonKey);
        log.info("Redis key[{}] 自减后：{}", jsonKey, number);
        return number;
    }

    public <K> Long increment(final K key) {
        String jsonKey = redisCacheSerializer.toJson(key);
        Long number = redisTemplate.opsForValue().increment(jsonKey);
        log.info("Redis key[{}] 自增后：{}", jsonKey, number);
        return number;
    }

    public <K> Boolean isExists(final K key) {
        String jsonKey = redisCacheSerializer.toJson(key);
        Boolean flag = redisTemplate.hasKey(jsonKey);
        log.info("查询 Redis 的键值是否存在\t[{}]-[{}]", jsonKey, flag);
        return flag;
    }

    /**
     * key 的类型在这个方法限制为 String
     * 获得缓存的基本对象列表
     * *：匹配任意数量个字符（包括 0 个字符）
     *
     * @param prefix 字符串前缀
     * @return 键的集合（全名）
     */
    public Set<String> getKeysByPrefix(final String prefix) {
        return getKeysByPattern(prefix + "*");
    }

    /**
     * key 的类型在这个方法限制为 String
     * 获得缓存的基本对象列表
     * *：匹配任意数量个字符（包括 0 个字符）
     * ?：匹配单个字符
     * []：匹配指定范围内的字符
     *
     * @param pattern 字符串格式
     * @return 键的集合（全名）
     */
    public Set<String> getKeysByPattern(final String pattern) {
        log.info("获取 Redis 格式为 [{}] 的键", pattern);
        return redisTemplate.keys(pattern);
    }

}