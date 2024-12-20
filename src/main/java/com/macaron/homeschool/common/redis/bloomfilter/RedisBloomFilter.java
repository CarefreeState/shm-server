package com.macaron.homeschool.common.redis.bloomfilter;

import cn.hutool.core.bean.BeanUtil;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;

import java.util.concurrent.TimeUnit;

public class RedisBloomFilter<T> {

    private String name;

    private Long preSize;

    private Double rate;

    private Long timeout;

    private TimeUnit unit;

    private RBloomFilter<T> rBloomFilter;

    protected final synchronized <P> void initFilter(RedissonClient redissonClient, P initialData) {
        BloomFilterProperties properties = BeanUtil.copyProperties(initialData, BloomFilterProperties.class);
        this.name = properties.getName();
        this.preSize = properties.getPreSize();
        this.rate = properties.getRate();
        this.timeout = properties.getTimeout();
        this.unit = properties.getUnit();
        this.rBloomFilter = redissonClient.getBloomFilter(this.name);
        tryInit();
    }

    public boolean isExists() {
        // 值得注意的是布隆过滤器的位图（本体）和布隆过滤器的配置是分开来存储的
        // 本体没了才返回 false，配置在不在不影响这里，但是配置不在了的话肯定会抛异常
        return rBloomFilter.isExists();
    }

    public void expire() {
        rBloomFilter.expire(timeout, unit);
    }

    public void tryInit() {
        rBloomFilter.tryInit(preSize, rate);
        expire();
    }

    public void add(T key) {
        try {
            rBloomFilter.add(key);
            expire();
        } catch (RedisException e) {
            tryInit();
            rBloomFilter.add(key);
        }
    }

    public boolean contains(T key) {
        try {
            boolean contains = rBloomFilter.contains(key);
            expire();
            return contains;
        } catch (RedisException e) {
            tryInit();
            return rBloomFilter.contains(key);
        }
    }

}