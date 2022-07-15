package com.f139.shop.admin.common.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeinConfig {

    @Bean
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                // 多久过期
                .expireAfterWrite(10, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(10)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

}