package com.support.config.security;

import com.support.mvc.entity.base.Result;
import com.support.mvc.enums.Code;
import com.utils.enums.ContentType;
import lombok.Cleanup;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 响应回调处理器；包含：未登录状态、登录成功状态、登录失败状态、退出操作成功状态
 *
 * @author 谢长春 2018/12/2
 */
public class AuthHandler implements
        AccessDeniedHandler,
        AuthenticationEntryPoint,
        AuthenticationSuccessHandler,
        AuthenticationFailureHandler,
        LogoutSuccessHandler{

    /**
     * 定制无权限响应状态：403；用户已登录，无访问权限
     */
    @Override
    public void handle(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final AccessDeniedException e) throws IOException {
        response.setContentType(ContentType.json.utf8());
        @Cleanup final PrintWriter writer = response.getWriter();
        writer.write(Code.ACCESS_DENIED.toResult("未授权的接口禁止访问").jsonFormat());
        writer.flush();
    }

    /**
     * 定制未登录响应状态：401；匿名用户访问需要授权的接口
     */
    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException e) throws IOException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        response.setContentType(ContentType.json.utf8());
        @Cleanup final PrintWriter writer = response.getWriter();
        writer.write(Code.TIMEOUT.toResult("未登录或登录超时").jsonFormat());
        writer.flush();
    }

    /**
     * 定制的 AuthenticationSuccessHandler；登录成功之后执行逻辑；如果指定了这个选项那么 loginSuccessUrl() 无效
     */
    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication) throws IOException {
        response.setContentType(ContentType.json.utf8());
        @Cleanup final PrintWriter writer = response.getWriter();
        writer.write(new Result<>().setSuccess(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).jsonFormat());
        writer.flush();
    }

    /**
     * 定制的 AuthenticationFailureHandler；登录失败将执行逻辑
     */
    @Override
    public void onAuthenticationFailure(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final AuthenticationException e) throws IOException {
        response.setContentType(ContentType.json.utf8());
        @Cleanup final PrintWriter writer = response.getWriter();
        writer.write(Code.FAILURE.toResult("登录失败").jsonFormat());
        writer.flush();
    }

    /**
     * 定制的 LogoutSuccessHandler；退出成功将执行逻辑
     */
    @Override
    public void onLogoutSuccess(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final Authentication e) throws IOException {
        response.setContentType(ContentType.json.utf8());
        @Cleanup final PrintWriter writer = response.getWriter();
        writer.write(Code.SUCCESS.toResult().toString());
        writer.flush();
    }

}
