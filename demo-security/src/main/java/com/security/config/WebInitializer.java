package com.security.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() { //根容器
        return new Class<?>[]{
//                SecurityConfig.class
//                MultiSecurityConfig.class,
//                DataSourceConfig.class
//                MongodbConfig.class,
//                RedisConfig.class
        };
    }

    /**
     * 加载spring MVC的配置
     *
     * @return Class
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{
                WebMvcConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() { //DispatcherServlet映射,从"/"开始
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{
                // 解决静态资源乱码
                new CharacterEncodingFilter("UTF-8", true)
        };
    }

}
