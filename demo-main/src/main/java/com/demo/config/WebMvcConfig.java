package com.demo.config;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.config.BusConfig;
import com.config.Initializer;
import com.demo.config.init.AppConfig;
import com.demo.config.interceptor.LogUserInterceptor;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Charsets.UTF_8;

/**
 * <pre>
 * Spring Core，参考：
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html
 *
 * Spring MVC 配置，参考 ：
 * https://linesh.gitbooks.io/spring-mvc-documentation-linesh-translation/content/
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html
 *
 * Spring Thymeleaf 配置，参考 ：
 * https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#spring-mvc-configuration
 *
 * Spring validator 配置，参考
 * https://beanvalidation.org/2.0/spec/#introduction
 * https://www.baeldung.com/javax-validation-method-constraints
 * http://rubygems.torquebox.org/proposals/BVAL-241/
 * https://juejin.im/entry/5b5a94d2f265da0f7c4fd2b2
 * https://www.cnblogs.com/mr-yang-localhost/p/7812038.html
 *
 * @author 谢长春 2018-10-3
 */
@Configuration
@Import(value = {BusConfig.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.demo"})
@EnableWebMvc
@PropertySource({"classpath:application.properties"})
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        // 这里执行在 Spring 初始化成功后的操作；因为在 Spring 未初始化完成之前，部分依赖注入的服务是不可用的
        this.applicationContext = applicationContext;
        // 初始化所有实现 Initializer 接口，且注解为 @Component 的类
        applicationContext.getBeansOfType(Initializer.class)
                .values()
                .stream()
                .sorted(Comparator.comparing(Initializer::priority))
                .forEach(Initializer::init);
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
     * 支持异步响应配置，参考：https://linesh.gitbooks.io/spring-mvc-documentation-linesh-translation/content/publish/21-3/4-asynchronous-request-processing.html
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    }

    /**
     * 添加自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器先截取将要记录到日志的用户信息
        registry.addInterceptor(new LogUserInterceptor()).addPathPatterns("/**");
    }

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
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES).cachePublic());
        // 添加静态资源过滤
        // 需要在 Spring Security 中配置忽略静态资源 WebSecurity.ignoring().antMatchers("/files/**");
        registry.addResourceHandler("/files/**")
                // Locations 可以指定绝对路径 file:d:\\files\\
                .addResourceLocations(String.format("file:%s", AppConfig.Path.ROOT.absolute()))
        ;
    }

    //    @Override
//    public void addFormatters(final FormatterRegistry registry) {
//        super.addFormatters(registry);
//        registry.addFormatter(varietyFormatter());
//        registry.addFormatter(dateFormatter());
//    }
//
//    /**
//     * 而VarietyFormatter可以自动转换我们的各种实体，将他们用在表单上（基本通过id）
//     * @return {@link VarietyFormatter}
//     */
//    @Bean
//    public VarietyFormatter varietyFormatter() {
//        return new VarietyFormatter();
//    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        // SpringResourceTemplateResolver automatically integrates with Spring's own
        // resource resolution infrastructure, which is highly recommended.
        final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
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
     //                .addProperty( "hibernate.validator.fail_fast", "true" )
     .buildValidatorFactory()
     .getValidator();
     }
     */
}
