package com.ccx.demo.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.ccx.demo.tl.DBContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

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
 * 分库分表及多数据源也可以使用： Sharding-JDBC
 *    参考配置：https://shardingsphere.apache.org/document/current/cn/overview/
 *
 * @author 谢长春 2019/1/23
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.ccx.**.dao.jpa"})
public class DBConfig {

    /**
     * 主库：数据源
     *
     * @return {@link javax.sql.DataSource}
     */
//    @Primary
    @Bean("masterDataSource")
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource() {
//        return DataSourceBuilder.create().build();
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 从库1：数据源
     *
     * @return {@link javax.sql.DataSource}
     */
    @Bean("secondDataSource")
    @ConfigurationProperties("spring.datasource.druid.second")
    public DataSource secondDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 从库2：数据源
     *
     * @return {@link javax.sql.DataSource}
     */
    @Bean("thirdDataSource")
    @ConfigurationProperties("spring.datasource.druid.third")
    public DataSource thirdDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 路由：数据源
     *
     * @param masterDataSource {@link DBConfig#masterDataSource()}
     * @param secondDataSource {@link DBConfig#secondDataSource()}
     * @param thirdDataSource  {@link DBConfig#thirdDataSource()}
     * @return {@link org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource} extend {@link javax.sql.DataSource}
     */
    @Bean("routingDataSource")
    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                        @Qualifier("secondDataSource") DataSource secondDataSource,
                                        @Qualifier("thirdDataSource") DataSource thirdDataSource
    ) {
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
     * <pre>
     * 这里是多数据源的关键：
     * 代理了目标数据源 dataSource 的所有方法，其中在 invoke 方法，Spring 使用了排除法
     * 只有 dataSource 获取到 {@link java.sql.Connection} 之后，在执行 {@link java.sql.Connection#prepareStatement(java.lang.String)} 时候，Spring 才会主动去数据库链接池中获取 {@link java.sql.Connection} ，
     * 这样做的好处就是提高数据库链接的使用率和效率，{@link LazyConnectionDataSourceProxy} 经常会被用在一些分库分表、多数据源事务的应用当中
     * 多数据源的事务管理解决方案，很多采用了同时开启所有数据源事务、同时提交的策略，例如：阿里的 cobar 解决方案等
     *
     * @param routingDataSource {@link DBConfig#routingDataSource(DataSource, DataSource, DataSource)} ()}
     * @return {@link org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy}
     */
    @Bean
    @Primary
    public LazyConnectionDataSourceProxy lazyConnectionDataSourceProxy(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    /**
     * 注册日志打印格式，
     * 参考：{@link com.alibaba.druid.spring.boot.autoconfigure.stat.DruidFilterConfiguration}
     *
     * @return {@link com.alibaba.druid.filter.logging.Slf4jLogFilter}
     */
//    @Bean
//    @ConfigurationProperties("spring.datasource.druid.filter.slf4j")
//    @ConditionalOnProperty(prefix = "spring.datasource.druid.filter.slf4j", name = "enabled")
//    @ConditionalOnMissingBean
//    public Slf4jLogFilter slf4jLogFilter() {
//        return new Slf4jLogFilter();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.druid.filter.stat")
//    @ConditionalOnProperty(prefix = "spring.datasource.druid.filter.stat", name = "enabled")
//    @ConditionalOnMissingBean
//    public StatFilter statFilter() {
//        return new StatFilter();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.druid.stat-view-servlet")
//    @ConditionalOnProperty(prefix = "spring.datasource.druid.stat-view-servlet", name = "enabled")
//    @ConditionalOnMissingBean
//    public DruidStatProperties.StatViewServlet statViewServlet() {
//        return new DruidStatProperties.StatViewServlet();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.druid.web-stat-filter")
//    @ConditionalOnProperty(prefix = "spring.datasource.druid.web-stat-filter", name = "enabled")
//    @ConditionalOnMissingBean
//    public DruidStatProperties.WebStatFilter webStatFilter() {
//        return new DruidStatProperties.WebStatFilter();
//    }

//    @Bean
//    public DruidStatProperties druidStatProperties(
//            DruidStatProperties.StatViewServlet statViewServlet,
//            DruidStatProperties.WebStatFilter webStatFilter
//    ) {
//        final DruidStatProperties properties = new DruidStatProperties();
//        properties.setStatViewServlet(statViewServlet);
//        properties.setWebStatFilter(webStatFilter);
//        return properties;
//    }

}
