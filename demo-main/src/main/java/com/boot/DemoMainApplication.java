package com.boot;

import com.boot.demo.config.init.AppProperties;
import com.support.config.BusConfig;
import com.support.config.InitConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

/**
 * <pre>
 * 参考配置：
 *   https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/
 *
 * @author 谢长春 2019/1/21
 */
@SpringBootApplication
@Import(value = {InitConfig.class, BusConfig.class})
@EnableConfigurationProperties(value = {AppProperties.class})
public class DemoMainApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(DemoMainApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(ApplicationContext context) {
//        return args -> {
//            System.out.println("打印所有bean:");
//            Stream.of(context.getBeanDefinitionNames()).sorted().forEach(System.out::println);
//        };
//    }
}
