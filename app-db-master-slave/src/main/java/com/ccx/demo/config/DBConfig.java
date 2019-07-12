package com.ccx.demo.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.ccx.demo.tl.DBContext;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

import static com.ccx.demo.enums.DBRoute.*;

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
 *
 * 多数据源或主从配置策略配置方案最终实现说明：
 *   先由使用 springboot 默认装配实现（简化配置）
 *   然后在自定义 @Bean 实现方法头加上 @Primary 让优先级高于 springboot 默认实现方法，达到覆盖 springboot 默认实现目的
 *   这样可以减少破坏 springboot 自动装配逻辑
 *
 * @author 谢长春 2019/1/23
 */
@Configuration
//@EnableTransactionManagement(order=2)
@EnableJpaRepositories(
        basePackages = {"com.ccx.**.dao"}
        )
public class DBConfig {

    /**
     * 主库：数据源
     *
     * @return {@link DataSource}
     */
    @Primary
    @Bean("masterDataSource")
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
//        return DataSourceBuilder.create().build();
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 从库1：数据源
     *
     * @return {@link DataSource}
     */
    @Bean("secondDataSource")
    @ConfigurationProperties("spring.datasource.second")
    public DataSource secondDataSource() {
//        return DataSourceBuilder.create().build();
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 从库2：数据源
     *
     * @return {@link DataSource}
     */
    @Bean("thirdDataSource")
    @ConfigurationProperties("spring.datasource.third")
    public DataSource thirdDataSource() {
//        return DataSourceBuilder.create().build();
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 路由：数据源
     *
     * @param masterDataSource {@link DBConfig#masterDataSource()}
     * @param secondDataSource {@link DBConfig#secondDataSource()}
     * @param thirdDataSource  {@link DBConfig#thirdDataSource()}
     * @return {@link AbstractRoutingDataSource} extend {@link DataSource}
     */
    @Bean("routingDataSource")
    public AbstractRoutingDataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                                       @Qualifier("secondDataSource") DataSource secondDataSource,
                                                       @Qualifier("thirdDataSource") DataSource thirdDataSource) {
        final AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return DBContext.get();
            }
        };
        routingDataSource.setDefaultTargetDataSource(masterDataSource);
        routingDataSource.setTargetDataSources(new HashMap<Object, Object>(3) {{
            put(MASTER, masterDataSource);
            put(SECOND, secondDataSource);
            put(THIRD, thirdDataSource);
        }});
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

    /**
     * 管理主从策略配置注入
     *
     * @param builder           {@link EntityManagerFactoryBuilder} springboot 简化配置时，用于自动装配，必须在 {@link DBConfig#masterDataSource()} 头上指定 @Primary，该对象才有效
     * @param routingDataSource {@link DBConfig#routingDataSource(DataSource, DataSource, DataSource)} 主从路由数据源
     * @return {@link LocalContainerEntityManagerFactoryBean}
     */
    @Bean("entityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
            EntityManagerFactoryBuilder builder,
            @Qualifier("routingDataSource") DataSource routingDataSource) {
        return builder
                .dataSource(routingDataSource)
//                .properties(jpaProperties.getProperties())
                .packages("com.ccx") // 设置实体类扫描包
//                .persistenceUnit("multiPersistenceUnit")
                .build();
    }

    /**
     * 自定义实体管理，覆盖 springboot 自动装配
     *
     * @param entityManagerFactoryBean {@link DBConfig#entityManagerFactoryBean(EntityManagerFactoryBuilder, DataSource)}
     * @return {@link EntityManagerFactory}
     */
    @Primary
    @Bean("entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return entityManagerFactoryBean.getObject();
    }

    /**
     * 自定义事务控制，覆盖 springboot 自动装配
     *
     * @param entityManagerFactory {@link DBConfig#entityManagerFactory(LocalContainerEntityManagerFactoryBean)}
     * @return {@link PlatformTransactionManager}
     */
    @Primary
    @Bean("transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    /**
     * 不加 @Primary {@link DBConfig#jpaQueryFactory(EntityManager)} 会报错
     *
     * @param entityManagerFactory {@link DBConfig#entityManagerFactory(LocalContainerEntityManagerFactoryBean)}
     * @return {@link EntityManager}
     */
    @Primary
    @Bean("entityManager")
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

}
