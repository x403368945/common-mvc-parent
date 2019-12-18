package com.ccx.demo.open.auth.cache;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ccx.demo.business.user.cache.ITabUserCache;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.config.init.BeanInitializer;
import com.support.mvc.enums.Code;
import com.utils.util.Base64;
import com.utils.util.Dates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.ccx.demo.config.init.BeanInitializer.Beans.cacheManager;

/**
 * token 登录模式缓存字段
 *
 * @author 谢长春 2019-12-16
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TokenCache implements ITabUserCache {
    private static final String CACHE_TOKEN = "TokenCache";

    /**
     * base64 解码并转换为 {@link TokenCache} 实体
     *
     * @param base64 {@link String} HTTP 请求头中获取到的 token
     * @return {@link TokenCache}
     */
    public static TokenCache decode(final String base64) {
        return Optional.ofNullable(Base64.decode(base64))
                .map(text -> {
                    final String[] split = text.split("");
                    return TokenCache.builder()
                            .userId(Long.parseLong(split[0]))
                            .uuid(split[0])
                            .build();
                })
                .orElseThrow(() -> new IllegalArgumentException("解码失败：".concat(base64)))
                .check();
    }

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 登录唯一标记
     */
    private String uuid;

    /**
     * 授权时间
     */
    @JSONField(serialzeFeatures = {SerializerFeature.UseISO8601DateFormat})
    private Timestamp timestamp;

    /**
     * 登录过期时间
     */
    @JSONField(serialzeFeatures = {SerializerFeature.UseISO8601DateFormat})
    private Timestamp expired;


    /**
     * 从缓存中获取用户信息
     *
     * @return {@link TabUser}
     */
    @JSONField(serialize = false, deserialize = false)
    public TabUser getUser() {
        return getTabUserCacheById(userId).orElseThrow(() -> new NullPointerException("用户不存在"));
    }

    /**
     * 生成 base64 编码
     *
     * @return {@link String}
     */
    public String token() {
        this.uuid = UUID.randomUUID().toString().replace("-", "");
        this.timestamp = Dates.now().timestamp();
        this.expired = Dates.now().addDay(30).timestamp();

        final Cache cache = Objects.requireNonNull(cacheManager.<CacheManager>get().getCache(CACHE_TOKEN), CACHE_TOKEN.concat(": 不能为 null"));
        cache.put(userId, this);

        return Base64.encode(String.format("%d:%s", userId, uuid));
    }

    /**
     * 用 base64 解码前端的 token 同缓存中的对象比对 token 是否过期，过期抛出异常，未过期则更新过期时间，并返回新的缓存对象，需要将新的缓存对象存入缓存中
     *
     * @return {@link TokenCache} 新的缓存对象
     */
    public TokenCache check() {
        final Cache cache = Objects.requireNonNull(cacheManager.<CacheManager>get().getCache(CACHE_TOKEN), CACHE_TOKEN.concat(": 不能为 null"));

        final TokenCache tokenCache = cache.get(CACHE_TOKEN.concat(Objects.toString(userId)), TokenCache.class);
        if (Objects.isNull(tokenCache)) {
            throw Code.TIMEOUT.exception("token 过期");
        }
        if (Objects.equals(this.uuid, tokenCache.getUuid())) {
            throw Code.TIMEOUT.exception("token 过期");
        }
        if (tokenCache.getTimestamp().getTime() - Instant.now().toEpochMilli() < 0) {
            throw Code.TIMEOUT.exception("token 过期");
        }

        // 更新过期时间
        tokenCache.setExpired(Dates.now().addDay(30).timestamp());
        cache.put(userId, tokenCache);

        return tokenCache;
    }
}