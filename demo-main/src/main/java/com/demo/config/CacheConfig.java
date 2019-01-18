package com.demo.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 *     Spring 缓存配置
 * @author 谢长春
 */
@Configuration
@EnableCaching
public class CacheConfig {
    /**
     * 用户昵称缓存
     */
    public static final String nicknameCache = "nicknameCache";

    @Bean
    public CacheManager cacheManager() {
        final SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Stream.of(
                nicknameCache
        ).map(ConcurrentMapCache::new).collect(Collectors.toList()));
        return manager;
    }
}