package com.config.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Spring Security 配置
 *
 *
 * @author 谢长春 2018/12/4
 */
public interface IAdapter {
    /**
     * 匹配表达式
     *
     * @return {@link String}
     */
    String matcher();

    /**
     * 配置
     *
     * @param http {@link HttpSecurity}
     * @throws Exception 配置初始化异常
     */
    void config(final HttpSecurity http) throws Exception;

    /**
     * Spring Security 开放接口配置，且支持跨域访问
     *
     * @author 谢长春 2018/12/4
     */
    interface Cors extends IAdapter {
        default String matcher() {
            return "/cors/**";
        }

        default void config(final HttpSecurity http) throws Exception {
            http
                    .antMatcher(matcher())
                    .csrf().disable()
                    .cors().and()
                    .anonymous().and()
                    .servletApi().and()
                    .headers().and()
                    .authorizeRequests()
                    .anyRequest().permitAll()
            ;
        }
    }

    /**
     * Spring Security 开放接口配置
     *
     * @author 谢长春 2018/12/4
     */
    interface Open extends IAdapter {
        default String matcher() {
            return "/open/**";
        }

        default void config(final HttpSecurity http) throws Exception {
            http
                    .antMatcher(matcher())
                    .csrf().disable()
                    .anonymous().and()
                    .servletApi().and()
                    .headers().and()
                    .authorizeRequests()
                    .anyRequest().permitAll()
            ;
            if (cors()) http.cors().and();
        }

        /**
         * 是否支持跨域
         * <pre>
         *     true：开启跨域；
         *     默认false；
         *     一般只建议在非生产环境开启跨域，若必须要在生产环境跨域，建议使用 {@link IAdapter.Cors}
         *
         * @return boolean
         */
        default boolean cors() {
            return false;
        }
    }

}
