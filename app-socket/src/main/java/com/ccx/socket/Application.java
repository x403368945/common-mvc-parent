package com.ccx.socket;

/**
 * spring-boot 特殊处理：只有 spring-boot 需要该文件
 * <pre>
 * 参考配置：
 *   https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/
 *
 * @author 谢长春 2019/1/29
 */
// spring-boot start >>
/*
@SpringBootApplication
@Import(value = {InitConfig.class})
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
*/
// spring-boot end <<<<