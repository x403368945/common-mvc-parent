package com.ccx.demo.open.auth.service;

import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.open.auth.cache.TokenCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 登录 token 缓存操作
 *
 * @author 谢长春 2019-12-16
 */
@Slf4j
@Service
public class TokenService {
    /**
     * 基于用户生成 token，每个用户只支持一个 token ，每次调用该方法，前一次生成的 token 将失效
     *
     * @param user {@link TabUser} 当前登录成功的用户
     * @return {@link String} 生成的 token
     */
    public String generate(final TabUser user) {
        final TokenCache tokenCache = TokenCache.builder()
                .userId(user.getId())
                .build();
        final String token = tokenCache.token();


        return token;
    }
}
