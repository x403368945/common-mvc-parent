package com.support.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求标记，存入 ThreadLocal，请求响应之后移除 ThreadLocal
 *
 * @author 谢长春 on 2018/1/28.
 * @deprecated 该类已废弃，已使用继承 {@link ch.qos.logback.classic.helpers.MDCInsertingServletFilter} 方案替代替代
 */
@Slf4j
@Deprecated
public class RequestIdInterceptor implements HandlerInterceptor {

    /**
     * 该方法将在请求处理之前进行调用，只有该方法返回true，
     * 才会继续执行后续的Interceptor和Controller，当返回值为true 时就会继续调用下一个Interceptor的preHandle 方法，
     * 如果已经是最后一个Interceptor的时候就会是调用当前请求的Controller方法；
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        log.info("{}#preHandle------------------------------------------------------------", this.getClass().getName());
//        response.addHeader("rid", RequestId.setRandomAlphanumeric());
        return true;
    }

    /*
     * 该方法也是需要当前对应的Interceptor的preHandle方法的返回值为true时才会执行，
     * 该方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
//        log.info("{}#postHandle------------------------------------------------------------", this.getClass().getName());
    }

    /*
     * 该方法将在请求处理之后，DispatcherServlet进行视图返回渲染之前进行调用，
     * 可以在这个方法中对Controller 处理之后的ModelAndView 对象进行操作。
     * 用于进行资源清理。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
//        log.info("{}#afterCompletion------------------------------------------------------", this.getClass().getName());
//        RequestId.remove();
    }
}