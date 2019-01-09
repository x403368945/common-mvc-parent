package com;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 缓存服务接口基础方法定义
 *
 * @author 谢长春 on 2018/2/8 .
 */
public interface ICache<V> {
    /**
     * Redis 数据库信息
     */
    @RequiredArgsConstructor(staticName = "of")
    @Data
    @Accessors(chain = true)
    class DBInfo {
        /**
         * 数据库索引
         */
        private final int index;
        /**
         * 数据库名称
         */
        private final String name;
        /**
         * 数据库说明
         */
        private final String comment;
        /**
         * 数据库服务类
         */
        private final String clazz;
    }

    /**
     * 获取数据库定义
     *
     * @return {@link DBInfo}
     */
    DBInfo getDB();

    /**
     * 获取 Redis 操作模板
     *
     * @return RedisTemplate
     */
    RedisTemplate<String, V> getRedisTemplate();

    /**
     * 初始化数据
     */
    default void init() {
        setDB();
    }

    /**
     * 是否启用 redis 缓存
     */
    default Optional<Boolean> use() {
        return Optional.of(Objects.nonNull(getRedisTemplate()));
    }

    /**
     * 初始化数据
     */
    default void setDB() {
        final DBInfo db = getDB();
        final BoundHashOperations<String, Object, Object> hash = getRedisTemplate().boundHashOps("db");
        hash.put("index", Objects.toString(db.getIndex()));
        hash.put("name", db.getName());
        hash.put("comment", db.getComment());
        hash.put("clazz", db.getClazz());
    }

    /**
     * 检测 key 是否存在
     *
     * @param key String
     * @return boolean, true存在，false不存在
     */
    default boolean hasKey(final String key) {
        return getRedisTemplate().hasKey(key);
    }

    /**
     * 设置过期时间
     *
     * @param key     String
     * @param timeout long
     * @param unit    TimeUnit
     * @return boolean
     */
    default boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return getRedisTemplate().expire(key, timeout, unit);
    }

    /**
     * 设置过期时间
     *
     * @param key  String
     * @param date Date
     * @return boolean
     */
    default boolean expireAt(final String key, final Date date) {
        return getRedisTemplate().expireAt(key, date);
    }

    /**
     * 删除缓存
     *
     * @param keys String[]
     */
    default void delete(final String... keys) {
        getRedisTemplate().delete(Arrays.asList(keys));
    }

    /**
     * 清除数据库
     */
    default void clear() {
        getRedisTemplate().execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(final RedisConnection conn) throws DataAccessException {
                conn.flushDb();
                return conn;
            }
        });
    }

    /**
     * 检测缓存是否需要刷新
     *
     * @return boolean true需要刷新，false不需要刷新
     */
    default boolean hasRefresh() {
        return !getRedisTemplate().opsForHash().hasKey("db", "status");
    }

    /**
     * 设置缓存刷新完成
     */
    default void refreshEnd() {
        getRedisTemplate().boundHashOps("db").put("status", "true");
    }
}
