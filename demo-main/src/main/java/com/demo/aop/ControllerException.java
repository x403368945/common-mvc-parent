package com.demo.aop;

import com.demo.config.WebInitializer;
import com.mvc.enums.Code;
import com.utils.enums.ContentType;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 请求异常处理
 *
 * @author 谢长春 2019/1/19
 */
@Slf4j
@ControllerAdvice
public class ControllerException {

    @SneakyThrows
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public void missingParamHandle(final HttpServletRequest request, final HttpServletResponse response, final MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        response.setContentType(ContentType.json.utf8());
        @Cleanup final PrintWriter writer = response.getWriter();
        writer.write(Code.ARGUMENT.toResult("请求 url 映射的方法缺少必要的参数").jsonFormat());
        writer.flush();
    }

    /**
     * <pre>
     * 404 异常
     *   需要在 {@link WebInitializer} 中添加以下代码
     *     \\@Override
     *     protected void customizeRegistration(ServletRegistration.Dynamic registration) {
     *         super.customizeRegistration(registration);
     *         registration.setInitParameter("throwExceptionIfNoHandlerFound", "true"); // 指定 404 抛出异常
     *     }
     */
    @SneakyThrows
    @ExceptionHandler(NoHandlerFoundException.class)
    public void noHandlerFound(final HttpServletRequest request, final HttpServletResponse response, final NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        response.setContentType(ContentType.json.utf8());
        @Cleanup final PrintWriter writer = response.getWriter();
        writer.write(Code.URL_MAPPING.toResult("404：请求url不存在").jsonFormat());
        writer.flush();
    }

    /**
     * 405 异常
     */
    @SneakyThrows
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void methodNotSupportHandle(final HttpServletRequest request, final HttpServletResponse response, final HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        response.setContentType(ContentType.json.utf8());
        @Cleanup final PrintWriter writer = response.getWriter();
        writer.write(Code.MAPPING.toResult("405：请求方式不被该接口支持，或者请求url错误未映射到正确的方法").jsonFormat());
        writer.flush();
    }

    @SneakyThrows
    @ExceptionHandler(Exception.class)
    public void methodNotSupportHandle(final HttpServletRequest request, final HttpServletResponse response, final Exception e) {
        log.error(e.getMessage(), e);
        response.setContentType(ContentType.json.utf8());
        @Cleanup final PrintWriter writer = response.getWriter();
        writer.write(Code.FAILURE.toResult(String.format("请求失败，不明确的异常：%s", e.getMessage())).jsonFormat());
        writer.flush();
    }
}
