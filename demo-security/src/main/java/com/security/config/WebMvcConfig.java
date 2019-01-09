package com.security.config;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.config.Initializer;
import com.security.config.init.AppConfig;
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
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
@ComponentScan(basePackages = {"com.security"})
@EnableWebMvc
@PropertySource({"classpath:application.properties"})
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer, ApplicationContextAware {

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        // 这里执行在 Spring 初始化成功后的操作；因为在 Spring 未初始化完成之前，部分依赖注入的服务是不可用的
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

//    /**
//     * 支持异步响应配置，参考：https://linesh.gitbooks.io/spring-mvc-documentation-linesh-translation/content/publish/21-3/4-asynchronous-request-processing.html
//     */
//    @Override
//    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//    }
//    /**
//     * 添加自定义拦截器
//     *
//     * @param registry
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 拦截器先截取将要记录到日志的用户信息
//        registry.addInterceptor(new LogUserInterceptor()).addPathPatterns("/**");
//    }

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
}
