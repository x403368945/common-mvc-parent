package com.security.config;

import com.config.security.IAdapter;
import com.config.security.IAdapter.Open;
import com.config.security.MemAdapter;
import com.security.config.init.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Spring Security 权限控制
 * <pre>
 *     中文文档：https://springcloud.cc/spring-security-zhcn.html
 *     官方文档：https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/reference/htmlsingle/
 *
 *
 * @author 谢长春 2017年7月7日 下午1:28:31
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
// 使用@EnableGlobalMethodSecurity注解来启用基于注解的安全性
// @EnableGlobalMethodSecurity(securedEnabled = true) // 启用注解：@Secured；[@Secured("ROLE_USER"), @Secured("IS_AUTHENTICATED_ANONYMOUSLY")]
// @EnableGlobalMethodSecurity(prePostEnabled = true) // 启用注解：@PreAuthorize；[@PreAuthorize("hasAuthority('ROLE_USER')"), @PreAuthorize("isAnonymous()")]
@Slf4j
public class SecurityConfig {
//    /**
//     * 自定义校验规则
//     */
//    @Configuration
//    public static class GlobalMethodSecurity extends GlobalMethodSecurityConfiguration {
//        @Override
//        protected MethodSecurityExpressionHandler createExpressionHandler() {
//            final DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
//            expressionHandler.setPermissionEvaluator(null);
//            return expressionHandler;
//        }
//    }

    /**
     * 跨域访问配置
     *
     * @return CorsConfigurationSource
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(
                "*"
//				"http://localhost",
//				"http://localhost:3000",
//				"http://192.168.0.201",
//				"http://192.168.0.201:3000",
//				"http://127.0.0.1",
//				"http://127.0.0.1:3000"
        ));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 开放接口，无需登录授权
     *
     * @author 谢长春 2017年7月7日 上午9:58:58
     */
    @Configuration
    @Order(1)
    public static class CorsSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter implements IAdapter.Cors {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            config(http);
        }
    }

    /**
     * 开放接口，无需登录授权
     *
     * @author 谢长春 2017年7月7日 上午9:58:58
     */
    @Configuration
    @Order(2)
    public static class OpenWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter implements IAdapter.Open {
        @Override
        public boolean cors() {
            return !AppConfig.isProd(); // ************************************************** 环境发布：非生产环境才能跨域
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            config(http);
        }
    }

    /**
     * 封闭接口，需登录授权
     *
     * @author 谢长春 2017年7月7日 上午9:59:45
     */
    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends MemAdapter {
        @Override
        public boolean cors() {
            return !AppConfig.isProd(); // ************************************************** 环境发布：非生产环境才能跨域
        }
    }

}
