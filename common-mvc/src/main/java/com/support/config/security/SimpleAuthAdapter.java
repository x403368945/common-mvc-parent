package com.support.config.security;

import com.log.Reqid;
import com.utils.util.Util;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * Spring Security 简单的授权认证配置；<br>
 * 中文文档：https://springcloud.cc/spring-security-zhcn.html <br>
 * 官方文档：https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/reference/htmlsingle/
 *
 * <pre>
 *     实现以下方法指定登录用户及权限
 *     \@Bean
 *     public UserDetailsService userDetailsService() {
 *         final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
 *         manager.createUser(User.builder().username("admin").password(passwordEncoder().encode("admin")).roles("USER", "ADMIN").build());
 *         manager.createUser(User.builder().username("user").password(passwordEncoder().encode("111111")).roles("USER").build());
 *         return manager;
 *     }
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
 *
 * @author 谢长春 2018/12/4
 */
public class SimpleAuthAdapter extends WebSecurityConfigurerAdapter {
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final AuthHandler authHandler = new AuthHandler();
//        /**
//         * {@link HeaderWriterFilter.java}
//         */
//        final HeaderWriter headerWriter = (request, response) -> {
//            final String uuid = Util.uuid();
//            response.addHeader("uuid", uuid);
//        };
        http
                .csrf().disable()
                // 允许跨域
                .cors().and()
                // http 响应头追加请求唯一标记
                .headers().addHeaderWriter((req, res) -> res.addHeader("uid", Reqid.getAndRemove())).and()
                // 用户访问未经授权的rest API，返回错误码401（未经授权）
                .exceptionHandling().authenticationEntryPoint(authHandler).accessDeniedHandler(authHandler)
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
            http.addFilterBefore(
                    new JsonUsernamePasswordAuthenticationFilter(authenticationManager(), authHandler, authHandler),
                    UsernamePasswordAuthenticationFilter.class
            );
            http.addFilterBefore((req, res, chain) -> {
                Reqid.set(Util.uuid()); // 设置请求唯一标记
                chain.doFilter(req, res);
            }, ChannelProcessingFilter.class);
        }
    }

    /**
     * 配置Spring Security的Filter链
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 解决静态资源被拦截的问题
        web.ignoring().antMatchers("/static/**", "/files/**", "/druid/**");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
