package com.socket.config;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Charsets.UTF_8;

/**
 * Spring MVC 配置
 * 参考 ： https://linesh.gitbooks.io/spring-mvc-documentation-linesh-translation/content/
 * Spring Thymeleaf 配置
 * 参考 ： https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#spring-mvc-configuration
 *
 *
 * @author 谢长春 2018-10-3
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.socket"})
@EnableWebMvc
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer, ApplicationContextAware {
    private static ApplicationContext APP_CONTEXT;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        // 这里执行在 Spring 初始化成功后的操作；因为在 Spring 未初始化完成之前，部分依赖注入的服务是不可用的
        // 初始化applicationContext
        APP_CONTEXT = applicationContext;
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

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        final Charset encoding = UTF_8;
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
     * 注入文件上传的bean
     */
    @Bean
    public MultipartResolver multipartResolver() {
        final CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        resolver.setDefaultEncoding(UTF_8.displayName());
        resolver.setMaxUploadSize(1048576000);
        return resolver;
    }

    /**
     * 支持异步响应配置，
     * 参考：https://linesh.gitbooks.io/spring-mvc-documentation-linesh-translation/content/publish/21-3/4-asynchronous-request-processing.html
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    }
//    /**
//     * 添加自定义拦截器
//     *
//     * @param registry
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        super.addInterceptors(registry);
//    }

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

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        // 添加静态资源过滤
        // 需要在 Spring Security 中配置忽略静态资源 WebSecurity.ignoring().antMatchers("/static/**");
        registry.addResourceHandler("/static/**")
                // Locations 这里应该是编译后的静态文件目录
                .addResourceLocations("/static/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES).cachePublic())
        ;
        // 添加静态资源过滤
        // 需要在 Spring Security 中配置忽略静态资源 WebSecurity.ignoring().antMatchers("/files/**");
//        registry.addResourceHandler("/files/**")
//                // Locations 可以指定绝对路径 file:d:\\files\\
//                .addResourceLocations(String.format("file:%s", AppConfig.Path.ROOT.absolute()))
//        ;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        // SpringResourceTemplateResolver automatically integrates with Spring's own
        // resource resolution infrastructure, which is highly recommended.
        final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(APP_CONTEXT);
//        resolver.setPrefix("/WEB-INF/classes/");
        resolver.setSuffix(".html");
        // HTML is the default value, added here for the sake of clarity.
        resolver.setTemplateMode(TemplateMode.HTML);
        // Template cache is true by default. Set to false if you want
        // templates to be automatically updated when modified.
        resolver.setCharacterEncoding("UTF-8");
//        resolver.setCacheable(true);
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        // SpringTemplateEngine automatically applies SpringStandardDialect and
        // enables Spring's own MessageSource message resolution mechanisms.
        final SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
        // speed up execution in most scenarios, but might be incompatible
        // with specific cases when expressions in one template are reused
        // across different data types, so this flag is "false" by default
        // for safer backwards compatibility.
        engine.setEnableSpringELCompiler(true);
        return engine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

}
