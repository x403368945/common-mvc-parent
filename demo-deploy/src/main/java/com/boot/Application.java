package com.boot;

import org.springframework.boot.SpringApplication;

/**
 * 组合打包所有发布模块，避免出现循环依赖
 *
 * @author 谢长春 2019/1/25
 */
public class Application {

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
