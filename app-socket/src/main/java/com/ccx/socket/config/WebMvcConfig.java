package com.ccx.socket.config;

import com.support.config.AbstractMvcConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

/**
 * <pre>
 * Spring MVC 配置
 * 参考 ： https://linesh.gitbooks.io/spring-mvc-documentation-linesh-translation/content/
 * Spring Thymeleaf 配置
 * 参考 ： https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#spring-mvc-configuration
 *
 * @author 谢长春 2018-10-3
 */
@Configuration
@Slf4j
public class WebMvcConfig extends AbstractMvcConfig {

    /**
     * 添加自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        // 添加静态资源过滤
        // 需要在 Spring Security 中配置忽略静态资源 WebSecurity.ignoring().antMatchers("/files/**");
//        registry.addResourceHandler("/files/**")
//        // TODO 采坑记录结尾带 / 和不带 / 的区别
//        //   假设请求url为：http://{{host}}:{{port}}/files/temp/a.txt
//        //   addResourceLocations 指定绝对路径
//        //   d:/files => d:/temp/a.txt
//        //   d:/files/ => d:/files/temp/a.txt
//                .addResourceLocations(String.format("file:%s/", AppConfig.Path.ROOT.absolute()))
//        ;
    }


}
