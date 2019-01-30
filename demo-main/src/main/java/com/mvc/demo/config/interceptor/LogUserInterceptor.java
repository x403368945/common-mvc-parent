package com.mvc.demo.config.interceptor;

import com.mvc.demo.business.user.entity.TabUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * 用户会话拦截器；将需要记录到日志中的用户信息存入 ThreadLocal
 *
 *
 * @author 谢长春 on 2018/1/28.
 */
@Slf4j
public class LogUserInterceptor implements HandlerInterceptor {
    private static final ThreadLocal<String> TL = ThreadLocal.withInitial(() -> "");

    /**
     * 该方法将在请求处理之前进行调用，只有该方法返回true，
     * 才会继续执行后续的Interceptor和Controller，当返回值为true 时就会继续调用下一个Interceptor的preHandle 方法，
     * 如果已经是最后一个Interceptor的时候就会是调用当前请求的Controller方法；
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .map(principal -> {
                    if (principal instanceof TabUser) {
                        final TabUser user = (TabUser) principal;
                        // 现在日志中只加了 用户ID 和 用户登录名 ；后面还可以加入手机号等其他信息
                        return String.format("%d:%s", user.getId(), user.getUsername());
                    }
                    return Objects.toString(principal);
                })
                .ifPresent(TL::set);
        return true;
    }

    /*
     * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行，
     * 该方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行。
     * 用于进行资源清理。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        TL.remove();
    }

    /*
     * 该方法将在请求处理之后，DispatcherServlet进行视图返回渲染之前进行调用，
     * 可以在这个方法中对Controller 处理之后的ModelAndView 对象进行操作。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }

    /**
     * 获取 ThreadLocal 将会被日志记录的用户信息
     *
     * @return {@link String}
     */
    public static String get() {
        return TL.get();
//        return Optional.ofNullable(TL.get()).orElse("");
    }
}
