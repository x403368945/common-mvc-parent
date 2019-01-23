package com.demo.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;

/**
 * <pre>
 * Spring Data JPA 配置
 *   参考配置：https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 *   QueryDSL
 *     参考配置：http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/#jpa_integration
 * Spring Data MongoDB 配置, AbstractMongoConfiguration 已经默认实现了 mongoTemplate和mongoFactoryBean
 *    参考配置：https://docs.spring.io/spring-data/mongodb/docs/2.0.0.RC3/reference/html/
 *    QueryDSL
 *      参考配置： http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/#mongodb_integration
 *
 * @author 谢长春 2019/1/23
 */
@Configuration
public class DBConfig {

    @Bean
    public JPAQueryFactory jpaQueryFactory(@Autowired EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MongoMappingContext context) {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), context);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        converter.setCustomConversions(new MongoCustomConversions(Collections.singletonList(
                new Converter<Date, Timestamp>() {
                    @Override
                    public Timestamp convert(Date date) {
                        return new Timestamp(date.getTime());
                    }
                }
        )));
        return new MongoTemplate(mongoDbFactory, converter);
    }
}
