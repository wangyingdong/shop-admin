package com.f139.shop.admin.common.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@ConditionalOnProperty(value = "spring.redis.host")
@Configuration
public class RedisConfig {

    @Autowired
    private RedisTemplate redisTemplate;


    @Bean
    public RedisTemplate redisTemplateInit() {
        //设置序列化Key的实例化对象
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置序列化Value的实例化对象
        redisTemplate.setValueSerializer(new GenericFastJsonRedisSerializer());

        // Hash key序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // Hash value序列化
        redisTemplate.setHashValueSerializer(new GenericFastJsonRedisSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}