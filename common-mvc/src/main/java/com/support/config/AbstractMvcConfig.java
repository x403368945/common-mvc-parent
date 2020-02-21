package com.support.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.support.mvc.entity.base.Result;
import com.support.mvc.enums.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Charsets.UTF_8;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * <pre>
 * mvc 基本配置
 * 请求异常拦截
 *
 * @author 谢长春 2019/1/23
 */
@ControllerAdvice
@Slf4j
public class AbstractMvcConfig implements WebMvcConfigurer {
// spring-boot start >>

    /**
     * spring-boot 特殊处理：添加异常处理
     * 服务端 500 异常处理
     * 需要自定义 Controller 继承 {@link AbstractMvcConfig.ErrorController}
     * spring security 需要添加 http.antMatchers("/error").permitAll()
     *
     * @author 谢长春
     */
    public static class ErrorController extends AbstractErrorController {
        public ErrorController(ErrorAttributes errorAttributes) {
            super(errorAttributes);
        }

        @Override
        public String getErrorPath() {
            return "/error";
        }

        /**
         * 处理服务端 500 异常
         */
        @RequestMapping(value = "/error", method = {GET, POST, PUT, PATCH, DELETE})
        @ResponseBody
        public Result<?> error() {
            return Code.FAILURE.toResult("500：请求失败，不明确的异常");
        }
    }
// spring-boot end <<<<

    /**
     * 多线程管理
     *
     * @return ExecutorService
     */
    @Bean(destroyMethod = "shutdownNow")
    public ExecutorService multiThread() {
        return new ThreadPoolExecutor(
                16,
                16 * 4,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), // 线程池队列，超过 1024 个任务将会抛出异常
//                    new LinkedBlockingQueue<>(100),
                new ThreadFactoryBuilder().setNameFormat("multi-thread-%d").build(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    /**
     * 单线程管理
     *
     * @return ExecutorService
     */
    @Bean(destroyMethod = "shutdownNow")
    public ExecutorService singleThread() {
        return new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), // 线程池队列，超过 1024 个任务将会抛出异常
                new ThreadFactoryBuilder().setNameFormat("single-thread-%d").build(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Result<?> missingParamHandle(final MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return Code.ARGUMENT.toResult("请求 url 映射的方法缺少必要的参数");
    }

    //    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public Result<?> badRequestException(final Exception e) {
        log.error(e.getMessage(), e);
        return Code.BAD_REQUEST.toResult("400：请求不存在");
    }

    //    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result<?> noHandlerFound(final AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return Code.ACCESS_DENIED.toResult("403：无操作权限");
    }

    /**
     * <pre>
     * 404 异常
     *   需要在 application.yml 中添加以下代码
     *   spring.mvc.throw-exception-if-no-handler-found: true # 出现错误时, 直接抛出异常(便于异常统一处理，否则捕获不到404)
     *   spring.resources.add-mappings: false # 404关闭资源映射，否则会去寻找静态资源
     */
//    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public Result<?> noHandlerFound(final NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return Code.URL_MAPPING.toResult("404：请求url不存在");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result<?> methodNotSupportHandle(final HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return Code.MAPPING.toResult("405：请求方式不被该接口支持，或者请求url错误未映射到正确的方法");
    }

    /**
     * 500 异常
     * spring-boot 特殊处理：这里配置不起作用，现在是按照继承 {@link org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController} 方案实现
     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public Result<?> exception(final Exception e) {
        log.error(e.getMessage(), e);
        return Code.FAILURE.toResult(String.format("500：请求失败，不明确的异常：%s", e.getMessage()));
    }


    /**
     * 禁止自动匹配路径；‘.’ 不作为匹配规则
     *
     * @param configurer {@link PathMatchConfigurer}
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask(); // 解决循环引用问题
        final FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setDefaultCharset(UTF_8);
        converter.setSupportedMediaTypes(
                Arrays.asList(
                        MediaType.APPLICATION_JSON,
                        MediaType.APPLICATION_OCTET_STREAM,
                        MediaType.APPLICATION_FORM_URLENCODED,
                        MediaType.TEXT_PLAIN
                )
        );
        converter.getFastJsonConfig().setFeatures(Feature.OrderedField);
        return converter;
    }

    /**
     * 启用 FastJson
     * spring-boot 需要在 pom 文件中移除 com.fasterxml.jackson.core 包
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        final Charset encoding = UTF_8;
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask(); // 解决循环引用问题
        {
            StringHttpMessageConverter converter = new StringHttpMessageConverter(encoding);
            converter.setSupportedMediaTypes(
                    Arrays.asList(
                            MediaType.APPLICATION_JSON,
                            MediaType.APPLICATION_OCTET_STREAM,
                            MediaType.APPLICATION_FORM_URLENCODED,
                            MediaType.TEXT_PLAIN,
                            MediaType.APPLICATION_XML,
                            MediaType.TEXT_XML,
                            MediaType.TEXT_HTML
                    )
            );
            converters.add(converter);
        }
        {
            final FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
            converter.setDefaultCharset(encoding);
            converter.setSupportedMediaTypes(
                    Arrays.asList(
                            MediaType.APPLICATION_JSON,
                            MediaType.APPLICATION_OCTET_STREAM,
                            MediaType.APPLICATION_FORM_URLENCODED,
                            MediaType.TEXT_PLAIN
                    )
            );
            converter.getFastJsonConfig().setFeatures(Feature.OrderedField);
            converters.add(converter);
        }
    }

    /**
     * 支持异步响应配置，参考：https://linesh.gitbooks.io/spring-mvc-documentation-linesh-translation/content/publish/21-3/4-asynchronous-request-processing.html
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        // 添加静态资源过滤
        registry.addResourceHandler("favicon.ico").addResourceLocations("classpath:/static/");
        // 需要在 Spring Security 中配置忽略静态资源 WebSecurity.ignoring().antMatchers("/static/**");
        registry.addResourceHandler("/static/**")
                // Locations 这里应该是编译后的静态文件目录
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES).cachePublic());
        // knife4j 增强 swagger 配置 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        // knife4j 增强 swagger 配置 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    }

    /**
     * Spring validator 方法级别的校验
     * https://www.baeldung.com/javax-validation-method-constraints
     * https://juejin.im/entry/5b5a94d2f265da0f7c4fd2b2
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /*
     * 快速失败返回模式，只要有一个异常就返回
     * https://www.cnblogs.com/mr-yang-localhost/p/7812038.html
     @Bean
     public Validator validator() {
     return Validation.byProvider(HibernateValidator.class)
     .configure()
     .failFast(true)
     .addProperty( "hibernate.validator.fail_fast", "true" )
     .buildValidatorFactory()
     .getValidator();
     }
     */
//    @Override
//    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
//        resolvers.add((request, response, o, e) -> {
//            try {
//                String message = "";
//                if (e instanceof MissingServletRequestParameterException) {
//                    message = Code.ARGUMENT.toResult("请求 url 映射的方法缺少必要的参数").jsonFormat();
//                } else if (e instanceof NoHandlerFoundException) {
//                    message = Code.URL_MAPPING.toResult("404：请求url不存在").jsonFormat();
//                } else if (e instanceof HttpRequestMethodNotSupportedException) {
//                    Code.MAPPING.toResult("405：请求方式不被该接口支持，或者请求url错误未映射到正确的方法").jsonFormat();
//                } else {
//                    message = Code.FAILURE.toResult(String.format("请求失败，不明确的异常：%s", e.getMessage())).jsonFormat();
//                }
//                log.error(e.getMessage(), e);
//                response.setContentType(ContentType.json.utf8());
//                @Cleanup final PrintWriter writer = response.getWriter();
//                writer.write(message);
//                writer.flush();
//            } catch (IOException ex) {
//                log.error(ex.getMessage(), ex);
//            }
//            return null;
//        });
//    }
}
