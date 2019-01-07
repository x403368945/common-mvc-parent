package com.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Collections;
import java.util.List;

/**
 * Spring Security 简单的授权认证配置；<br>
 * 中文文档：https://springcloud.cc/spring-security-zhcn.html <br>
 * 官方文档：https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/reference/htmlsingle/
 *
 * <pre>
 *     在内存中产生两个用户，用户名:密码
 *     admin:admin
 *     user:111111
 *     可以通过 {@link MemAdapter#users()} 方法追加用户
 *
 *     登录url:/login
 *     支持三种登录传参
 *
 *     ### 登录： JSON 模式
 *         POST http://localhost:8080/login
 *          Content-Type: application/json
 *
 *          {"json": {"username": "admin","password": "admin"}}
 *     ###
 *     ### 登录：表单模式
 *         POST http://localhost:8080/login
 *          Content-Type: application/x-www-form-urlencoded
 *
 *          username=admin&password=admin
 *     ###
 *     ### Basic 模式，无 session
 *         GET http://localhost:8080/user
 *         Content-Type: application/json
 *         Authorization: basic YWRtaW46c3VwZXJhZG1pbg==
 *     ###
 *
 * @author 谢长春 2018/12/4
 */
public class MemAdapter extends WebSecurityConfigurerAdapter {
    /**
     * 是否支持跨域
     * <pre>
     *     true：开启跨域；
     *     默认false；
     *     一般只建议在非生产环境开启跨域，若必须要在生产环境跨域，建议使用 {@link IAdapter}
     *
     * @return boolean
     */
    protected boolean cors() {
        return false;
    }

    /**
     * 追加用户
     *
     * @return {@link List<UserDetails>}
     */
    protected List<? extends UserDetails> users() {
        return Collections.emptyList();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.builder().username("admin").password(passwordEncoder().encode("admin")).roles("USER", "ADMIN").build());
        manager.createUser(User.builder().username("user").password(passwordEncoder().encode("111111")).roles("USER").build());
        users().forEach(user -> manager.createUser(user));
        return manager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final AuthHandler authHandler = new AuthHandler();
        http
                .csrf().disable()
                // 允许跨域
                .cors().and()
                // 用户访问未经授权的rest API，返回错误码401（未经授权）
                .exceptionHandling().authenticationEntryPoint(authHandler)
                .and().authorizeRequests()
                // TODO 建议在类头部或方法头上加权限注解，这里配置太多的权限容易混淆
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().formLogin()
                .successHandler(authHandler)
                .failureHandler(authHandler)

                // 配置退出参数
                .and().logout()
                .logoutSuccessHandler(authHandler)
                .invalidateHttpSession(true) // 指定是否在注销时让HttpSession无效, 默认设置为 true
                .deleteCookies("JSESSIONID") // 删除 JSESSIONID
                .addLogoutHandler(new SecurityContextLogoutHandler()) // 添加一个LogoutHandler, // 清除 session
        ;

        if (cors()) {
            http.cors().and();
        }
        { // 自定义过滤器
            // token 校验前置过滤器
//                http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
            http.addFilterBefore(
                    new JsonUsernamePasswordAuthenticationFilter(authenticationManager(), authHandler, authHandler),
                    UsernamePasswordAuthenticationFilter.class
            );
        }
    }

    /**
     * 配置Spring Security的Filter链
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 解决静态资源被拦截的问题
        web.ignoring().antMatchers("/static/**", "/files/**");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
