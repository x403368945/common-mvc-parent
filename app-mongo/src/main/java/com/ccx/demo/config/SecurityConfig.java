package com.ccx.demo.config;

import com.alibaba.fastjson.JSON;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.config.init.AppConfig;
import com.ccx.demo.enums.Session;
import com.ccx.demo.open.auth.cache.TokenCache;
import com.support.config.security.AuthHandler;
import com.support.config.security.IAdapter;
import com.support.config.security.JsonUsernamePasswordAuthenticationFilter;
import com.support.config.security.SimpleAuthAdapter;
import com.support.filter.RequestIdFilter;
import com.support.mvc.enums.Code;
import com.support.mvc.exception.CodeException;
import com.support.mvc.exception.TokenNotExistException;
import com.utils.enums.ContentType;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Optional;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;

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
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
// 使用 @EnableGlobalMethodSecurity 注解来启用基于注解的安全性
// @EnableGlobalMethodSecurity(securedEnabled = true) // 启用注解：@Secured；[@Secured("ROLE_USER"), @Secured("IS_AUTHENTICATED_ANONYMOUSLY")]
// @EnableGlobalMethodSecurity(prePostEnabled = true) // 启用注解：@PreAuthorize；[@PreAuthorize("hasAuthority('ROLE_USER')"), @PreAuthorize("isAnonymous()")]
@Slf4j
public class SecurityConfig {
    private static AppConfig appConfig;

    public SecurityConfig(AppConfig appConfig) {
        SecurityConfig.appConfig = appConfig;
    }
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
        final CorsConfiguration configuration = new CorsConfiguration();
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
            return !appConfig.isProd(); // ************************************************** 环境发布：非生产环境才能跨域
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
    public static class FormLoginWebSecurityConfigurerAdapter extends SimpleAuthAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            final AuthHandler authHandler = new AuthHandler();
            http
                    .csrf().disable()
//                    .csrf().ignoringAntMatchers("/druid/*").and()
//                    .cors().and()
                    // http 响应头追加请求标记
//                    .headers().addHeaderWriter(requestIdFilter::writeHeaders).and()
                    //用户访问未经授权的rest API，返回错误码401（未经授权）
                    .exceptionHandling().authenticationEntryPoint(authHandler).accessDeniedHandler(authHandler)
//                    // 指定会话策略；ALWAYS:总是创建HttpSession, IF_REQUIRED:只会在需要时创建一个HttpSession, NEVER:不会创建HttpSession，但如果它已经存在，将可以使用HttpSession, STATELESS:永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext
//                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 基于Token，不需要Session
                    .and().authorizeRequests()
                    // TODO 建议在类头部或方法头上加权限注解，这里配置太多的权限容易混淆
                    .antMatchers("/").permitAll()
                    // spring-boot 特殊处理：HomeController 中需要异常处理方法
                    .antMatchers("/error").permitAll()
                    // 所有方法都需要登录认证
                    .anyRequest().authenticated()
//                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    // 开启 Basic 认证
                    .and().httpBasic()
                    .and().formLogin()
                    .successHandler(authHandler)
                    .failureHandler(authHandler)
            ;
            if (!appConfig.isProd()) {
                http.cors().and(); // ******************************************************* 环境发布：非生产环境才能跨域
            }
            // 启用缓存
//            httpSecurity.headers().cacheControl();
            { // 配置退出参数
                http.logout()
                        .logoutSuccessHandler(authHandler)
                        .invalidateHttpSession(true) // 指定是否在注销时让HttpSession无效, 默认设置为 true
                        .deleteCookies("JSESSIONID") // 删除 JSESSIONID
                        .addLogoutHandler(new SecurityContextLogoutHandler()) // 添加一个LogoutHandler, 清除 session
//                        .addLogoutHandler((request, response, authentication) ->  // 添加一个LogoutHandler, 退出操作需要删除 redis token 缓存
//                                TokenCache.instance().destroy(request.getHeader(Session.token.name()))
//                        )
                ;
            }
            { // 自定义登录验证适配器验证
//                httpSecurity.authenticationProvider(new AuthenticationProvider());
            }
            { // 自定义过滤器
                http.addFilterBefore(
                        new JsonUsernamePasswordAuthenticationFilter(authenticationManager(), authHandler, authHandler),
                        UsernamePasswordAuthenticationFilter.class
                );
                // 请求标记过滤器注册到 Spring Security 过滤器前面； ChannelProcessingFilter.class, SecurityContextPersistenceFilter.class
                http.addFilterBefore(new RequestIdFilter(), ChannelProcessingFilter.class);
                // token 模式
                http.addFilterAfter(authTokenFilter, BasicAuthenticationFilter.class);
            }
        }

//        /**
//         * 自定义认证方式
//         * @param auth AuthenticationManagerBuilder
//         * @throws Exception 初始化异常
//         */
//        @Override
//        protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
//            final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//            provider.setHideUserNotFoundExceptions(false);
//            provider.setUserDetailsService(this.userDetailsService);
//            provider.setPasswordEncoder(passwordEncoder());
//            auth.authenticationProvider(provider);
////          auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
//        }

        /**
         * token 验证模式
         */
        private final OncePerRequestFilter authTokenFilter = new OncePerRequestFilter() {

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                if (StringUtils.isBlank(request.getHeader(Session.token.name()))) {
                    filterChain.doFilter(request, response);
                    return;
                }
                try {
                    final String auth = request.getHeader(Session.token.name());
                    if (log.isTraceEnabled()) {
                        final Enumeration<String> headerNames = request.getHeaderNames();
                        final LinkedHashMap<String, String> obj = new LinkedHashMap<>();
                        while (headerNames.hasMoreElements()) {
                            val key = headerNames.nextElement();
                            obj.put(key, request.getHeader(key));
                        }
                        log.trace(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> headers\n{}", JSON.toJSONString(obj, PrettyFormat));
                    }

                    if (StringUtils.isBlank(auth)) {
                        throw new TokenNotExistException("token 不存在");
                    }
                    // token 校验成功之后，将用户信息设置到 SecurityContextHolder
                    final TabUser user = TokenCache.auth(auth).getUser();
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
                    filterChain.doFilter(request, response);
                    Optional.ofNullable(request.getSession(false)).ifPresent(session -> {
                        session.setMaxInactiveInterval(1); // 兼容默认的 session 模式，禁止 token 模式创建 session，设置 session 超时时间为1s
                    });
                } catch (Exception e) {
                    if (e instanceof TokenNotExistException || e instanceof CodeException) {
                        log.warn(e.getMessage());
                    } else {
                        log.error(e.getMessage(), e);
                    }
                    response.setContentType(ContentType.json.utf8());
                    @Cleanup final PrintWriter writer = response.getWriter();
                    writer.write(Code.TIMEOUT.toResult().json());
                    writer.flush();
                }
            }
        };
    }

}
