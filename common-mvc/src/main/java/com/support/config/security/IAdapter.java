package com.support.config.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.Optional;

/**
 * Spring Security 配置
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

        default RequestIdFilter getRequestIdFilter() {
            return null;
        }

        default void config(final HttpSecurity http) throws Exception {
            http
                    .antMatcher(matcher())
                    .csrf().disable()
                    .headers().addHeaderWriter((req, res) -> Optional.ofNullable(getRequestIdFilter()).ifPresent(requestIdFilter -> requestIdFilter.writeHeaders(req, res))).and()
                    .cors().and()
                    .anonymous().and()
                    .servletApi().and()
                    .headers().and()
                    .authorizeRequests()
                    .anyRequest().permitAll()
            ;
            Optional.ofNullable(getRequestIdFilter()).ifPresent(requestIdFilter -> requestIdFilter.setRequestIdFilter(http));
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

        default RequestIdFilter getRequestIdFilter() {
            return null;
        }

        default void config(final HttpSecurity http) throws Exception {
            http
                    .antMatcher(matcher())
                    .csrf().disable()
                    .headers().addHeaderWriter((req, res) -> Optional.ofNullable(getRequestIdFilter()).ifPresent(requestIdFilter -> requestIdFilter.writeHeaders(req, res))).and()
                    .anonymous().and()
                    .servletApi().and()
                    .headers().and()
                    .authorizeRequests()
                    .anyRequest().permitAll()
            ;
            if (cors()) http.cors().and();
            Optional.ofNullable(getRequestIdFilter()).ifPresent(requestIdFilter -> requestIdFilter.setRequestIdFilter(http));
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
