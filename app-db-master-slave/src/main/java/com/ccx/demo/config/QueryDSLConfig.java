package com.ccx.demo.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * spring-boot 特殊处理：大多数配置都在 application.yml 文件中完成，简化了数据库配置
 * JPA + MongoDB 部分自定义配置
 * <pre>
 * Spring Data JPA 配置
 *   参考配置：https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 *   QueryDSL
 *     参考配置：http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/#jpa_integration
 * Spring Data MongoDB 配置, AbstractMongoConfiguration 已经默认实现了 mongoTemplate和mongoFactoryBean
 *    参考配置：https://docs.spring.io/spring-data/mongodb/docs/2.0.0.RC3/reference/html/
 *    QueryDSL
 *      参考配置： http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/#mongodb_integration
 * 分库分表及多数据源也可以使用： Sharding-JDBC
 *    参考配置：https://shardingsphere.apache.org/document/current/cn/overview/
 *
 * @author 谢长春 2019/1/23
 */
//@Configuration
//public class QueryDSLConfig {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Bean
//    public JPAQueryFactory jpaQueryFactory() {
//        return new JPAQueryFactory(entityManager);
//    }
//}
