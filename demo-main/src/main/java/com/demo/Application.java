package com.demo;

import com.config.BusConfig;
import com.config.InitConfig;
import com.demo.config.init.AppProperties;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.Arrays;

/**
 * <pre>
 * 参考配置：
 *   https://docs.spring.io/spring-boot/docs/2.1.1.RELEASE/reference/htmlsingle/
 *
 * @author 谢长春 2019/1/21
 */

@SpringBootApplication(scanBasePackages = {"com.demo"})
@Import(value = {InitConfig.class, BusConfig.class})
@EnableConfigurationProperties(value = {AppProperties.class})
//@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

    /**
     * <pre>
     * Spring Data JPA 配置
     *   参考配置：https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
     * QueryDSL
     *   参考配置：http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/#jpa_integration
     * @author 谢长春
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(@Autowired EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
