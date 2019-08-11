package com.ccx.security.config;

import com.ccx.security.config.init.AppConfig;
import com.support.config.AbstractMvcConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

/**
 * Spring MVC 配置
 * 参考 ： https://linesh.gitbooks.io/spring-mvc-documentation-linesh-translation/content/
 * Spring Thymeleaf 配置
 * 参考 ： https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#spring-mvc-configuration
 *
 * @author 谢长春 2018-10-3
 */
@Configuration
@Slf4j
// spring-mvc start >>
/*
@Import(value = {InitConfig.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.ccx"})
@EnableWebMvc
@PropertySource({"classpath:application.properties"})
*/
// spring-mvc end <<<<
public class WebMvcConfig extends AbstractMvcConfig {

    // spring-mvc start >>
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
    // spring-mvc end <<<<

    /**
     * 添加自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 没有使用 security 时，可以使用拦截器拦截请求，设置请求标记
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
