package com.ccx.demo.config;

import com.ccx.demo.config.init.AppConfig;
import com.support.config.AbstractMvcConfig;
import com.utils.util.Dates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.sql.Timestamp;
import java.util.Date;

import static com.utils.util.Dates.Pattern.yyyy_MM_dd_HH_mm_ss_SSS;

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
public class WebMvcConfig extends AbstractMvcConfig {

    @Autowired
    private AppConfig appConfig;

    /**
     * 添加自定义拦截器
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
                //   假设请求url为：http://{{host}}:{{port}}/files/temp/a.txt
                //   addResourceLocations 指定绝对路径
                //   d:/files => d:/temp/a.txt
                //   d:/files/ => d:/files/temp/a.txt
                .addResourceLocations(String.format("file:%s/", appConfig.getPathRoot()))
        ;
    }

//    /**
//     * 在 Spring Security 中注册过滤器
//     * <pre>
//     * 添加自定义过滤器：设置请求标记，在日志打印和响应头中追加该标记
//     * 警告：多线程时需要特殊处理
//     * final Map<String, String> mdc = MDC.getCopyOfContextMap(); // 复制主线程 ThreadLocal
//     * new Thread(() -> {
//     *     try {
//     *         MDC.setContextMap(mdc); // 设置子线程 ThreadLocal
//     *         // 子线程代码
//     *     } finally {
//     *         MDC.clear(); // 清除子线程 ThreadLocal
//     *     }
//     * }).start();
//     *
//     * @return FilterRegistrationBean
//     */
//    @Bean
//    public FilterRegistrationBean filterRequestId() {
//        final FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
//        bean.setOrder(0);
//        bean.setFilter(new RequestId());
//        bean.addUrlPatterns("/*");
//        return bean;
//    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addFormatter(new VarietyFormatter()); // 而VarietyFormatter可以自动转换我们的各种实体，将他们用在表单上（基本通过id）
        registry.addConverter(new Converter<String, Date>() {
            @Override
            public Date convert(final String dateString) {
                return yyyy_MM_dd_HH_mm_ss_SSS.parseOfNullable(dateString).map(Dates::date).orElse(null);
            }
        });
        registry.addConverter(new Converter<String, Timestamp>() {
            @Override
            public Timestamp convert(final String dateString) {
                return yyyy_MM_dd_HH_mm_ss_SSS.parseOfNullable(dateString).map(Dates::timestamp).orElse(null);
            }
        });
//        registry.addConverter(new Converter<String, Object[]>() {
//            @Override
//            public Object[] convert(final String dataString) {
//                log.info(dataString);
////                return yyyy_MM_dd_HH_mm_ss_SSS.parseOfNullable(dateString).map(Dates::timestamp).orElse(null);
//                return null;
//            }
//        });
    }
}
