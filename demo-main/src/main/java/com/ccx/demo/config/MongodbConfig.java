package com.ccx.demo.config;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Spring Data MongoDB 配置
 * AbstractMongoConfiguration 已经默认实现了 mongoTemplate和mongoFactoryBean
 * 参考配置：https://docs.spring.io/spring-data/mongodb/docs/2.0.0.RC3/reference/html/
 * QueryDSL 参考配置： http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/#mongodb_integration
 *
 * @author 谢长春
 */
// spring-mvc start >> mvc 将 MySQL 和 MongoDB 配置分离
@Configuration
@EnableMongoRepositories(basePackages = {"com.ccx.**.dao.mongo"})
public class MongodbConfig {

    public class TimestampConverter implements Converter<Date, Timestamp> {
        @Override
        public Timestamp convert(Date date) {
            return new Timestamp(date.getTime());
        }
    }

    //
//    @ReadingConverter
//    public class InstantConverter implements Converter<LocalDateTime, Instant> {
//        @Override
//        public Instant convert(LocalDateTime localDateTime) {
//            return Instant.from(localDateTime);
//        }
//    }
//
//    @WritingConverter
//    public class LocalDateTimeConverter implements Converter<Instant, LocalDateTime> {
//        @Override
//        public LocalDateTime convert(Instant instant) {
//            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
//        }
//    }
//
    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new TimestampConverter()
//                new InstantConverter(),
//                new LocalDateTimeConverter()
        ));
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory mongoDbFactory, MongoMappingContext context, MongoCustomConversions mongoCustomConversions, BeanFactory beanFactory) {
        final MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), context);
//        converter.setCustomConversions(beanFactory.getBean(MongoCustomConversions.class));
        converter.setTypeMapper(new DefaultMongoTypeMapper(null)); // typeKey为null的时候，插入mongodb 不会产生 _class 属性
        converter.setCustomConversions(mongoCustomConversions); // 添加自定义的转换器
        return converter;
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory dbFactory, MappingMongoConverter converter) {
        return new MongoTemplate(dbFactory, converter);
    }

}