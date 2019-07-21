package com.ccx.demo.config;

import com.ccx.demo.config.init.AppConfig;
import com.support.config.AbstractMvcConfig;
import com.support.config.BusConfig;
import com.support.config.InitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import static com.google.common.base.Charsets.UTF_8;

/**
 * spring-boot 特殊处理：大部分配置在 application.yml 文件中，简化配置
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
@Slf4j
// spring-mvc start >>
//@Import(value = {InitConfig.class, BusConfig.class})
//@EnableAspectJAutoProxy(proxyTargetClass = true)
//@ComponentScan(basePackages = {"com.ccx"})
//@EnableWebMvc
//@PropertySource({"classpath:application.properties"})
// spring-mvc end <<<<
public class WebMvcConfig extends AbstractMvcConfig
// spring-mvc start >>
//        implements ApplicationContextAware
// spring-mvc end <<<<
{
    // spring-mvc start >>
//    private ApplicationContext applicationContext;
//
//    @Override
//    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
//
//    /**
//     * 注入文件上传的bean
//     */
//    @Bean
//    public MultipartResolver multipartResolver() {
//        final CommonsMultipartResolver resolver = new CommonsMultipartResolver();
////        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
//        resolver.setDefaultEncoding(UTF_8.displayName());
//        resolver.setMaxUploadSize(1048576000);
//        return resolver;
//    }
//
//    //    @Override
////    public void addFormatters(final FormatterRegistry registry) {
////        super.addFormatters(registry);
////        registry.addFormatter(varietyFormatter());
////        registry.addFormatter(dateFormatter());
////    }
////
////    /**
////     * 而VarietyFormatter可以自动转换我们的各种实体，将他们用在表单上（基本通过id）
////     * @return {@link VarietyFormatter}
////     */
////    @Bean
////    public VarietyFormatter varietyFormatter() {
////        return new VarietyFormatter();
////    }
//
//    @Bean
//    public SpringResourceTemplateResolver templateResolver() {
//        // SpringResourceTemplateResolver automatically integrates with Spring's own
//        // resource resolution infrastructure, which is highly recommended.
//        final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//        resolver.setApplicationContext(applicationContext);
////        resolver.setPrefix("/WEB-INF/classes/");
//        resolver.setPrefix("classpath:");
//        resolver.setSuffix(".html");
//        // HTML is the default value, added here for the sake of clarity.
//        resolver.setTemplateMode(TemplateMode.HTML);
//        // Template cache is true by default. Set to false if you want
//        // templates to be automatically updated when modified.
//        resolver.setCharacterEncoding("UTF-8");
////        resolver.setCacheable(true);
//        return resolver;
//    }
//
//    @Bean
//    public SpringTemplateEngine templateEngine() {
//        // SpringTemplateEngine automatically applies SpringStandardDialect and
//        // enables Spring's own MessageSource message resolution mechanisms.
//        final SpringTemplateEngine engine = new SpringTemplateEngine();
//        engine.setTemplateResolver(templateResolver());
//        // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
//        // speed up execution in most scenarios, but might be incompatible
//        // with specific cases when expressions in one template are reused
//        // across different data types, so this flag is "false" by default
//        // for safer backwards compatibility.
//        engine.setEnableSpringELCompiler(true);
//        return engine;
//    }
//
//    @Bean
//    public ThymeleafViewResolver viewResolver() {
//        final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//        resolver.setTemplateEngine(templateEngine());
//        resolver.setCharacterEncoding("UTF-8");
//        return resolver;
//    }
    // spring-mvc end <<<<

    /**
     * 添加自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 没有使用 security 时，可以使用拦截器拦截请求，设置请求唯一标记
//        registry.addInterceptor(new RequestIdInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        // 添加静态资源过滤
        // 需要在 Spring Security 中配置忽略静态资源 WebSecurity.ignoring().antMatchers("/files/**");
        registry.addResourceHandler("/files/**")
                // TODO 采坑记录结尾带 / 和不带 / 的区别
                //   假设请求url为：http://localhost:8080/files/temp/a.txt
                //   addResourceLocations 指定绝对路径
                //   d:/files => d:/temp/a.txt
                //   d:/files/ => d:/files/temp/a.txt
                .addResourceLocations(String.format("file:%s/", AppConfig.Path.ROOT.absolute()))
        ;
    }

}
