package com.ccx.demo;

import com.ccx.demo.config.init.AppProperties;
import com.support.config.BusConfig;
import com.support.config.InitConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

/**
 * spring-boot 特殊处理：只有 spring-boot 需要该文件
 * <pre>
 * 参考配置：
 *   https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/
 *
 * @author 谢长春 2019/1/21
 */
// spring-boot start >>
@SpringBootApplication
@Import(value = {InitConfig.class, BusConfig.class})
@EnableConfigurationProperties(value = {AppProperties.class})
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(ApplicationContext context) {
//        return args -> {
//            System.out.println("打印所有bean:");
//            Stream.of(context.getBeanDefinitionNames()).sorted().forEach(System.out::println);
//        };
//    }
}
// spring-boot end <<<<