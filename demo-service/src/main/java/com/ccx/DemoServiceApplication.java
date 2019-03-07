package com.ccx;

import org.springframework.boot.SpringApplication;

/**
 * <pre>
 * 参考配置：
 *   https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/
 *
 * @author 谢长春 2019/1/21
 */
public class DemoServiceApplication {
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
