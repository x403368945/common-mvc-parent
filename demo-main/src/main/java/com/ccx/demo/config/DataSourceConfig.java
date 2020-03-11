package com.ccx.demo.config;

/**
 * <pre>
 * Spring Data JPA 配置
 *   参考配置：https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 * QueryDSL
 *   参考配置：http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/#jpa_integration
 * Hibernate 命名策略可自定义
 *   参考配置：https://www.baeldung.com/hibernate-naming-strategy
 *
 * @author 谢长春
 */
// spring-mvc start >> mvc 将 MySQL 和 MongoDB 配置分离
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = {"com.ccx.**.dao.jpa"})
//@Slf4j
//public class DataSourceConfig {
//    @Value("${db.driver}")
//    private String driver;
//    @Value("${db.url}")
//    private String url;
//    @Value("${db.user}")
//    private String user;
//    @Value("${db.password}")
//    private String password;
//    @Value("${db.type}")
//    private String type;
//    @Value("${db.dialect}")
//    private String dialect;
//    @Value("${db.show_sql}")
//    private Boolean showSql;
//    @Value("${db.format_sql}")
//    private Boolean formatSql;
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
//        final LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
//        entityManagerFactory.setDataSource(dataSource());
//        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
//        entityManagerFactory.setJpaProperties(hibernateProperties());
//        entityManagerFactory.setPackagesToScan("com.ccx"); // 设置实体类所在位置
//        entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
////        entityManagerFactory.setPersistenceUnitName(""); // 持久化单元创建一个默认即可，多个便要分别命名
//        return entityManagerFactory;
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(driver);
//        dataSource.setUrl(url);
//        dataSource.setUsername(user);
//        dataSource.setPassword(password);
//        return dataSource;
//    }
//
//    private Properties hibernateProperties() {
//        final Properties properties = new Properties();
//        properties.put("hibernate.dialect", dialect);
//        properties.put("hibernate.show_sql", showSql);
//        properties.put("hibernate.format_sql", formatSql);
//        return properties;
//    }
//
//    @Bean
//    public HibernateJpaVendorAdapter jpaVendorAdapter() {
//        final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
//        adapter.setDatabase(Database.valueOf(type));
//        adapter.setShowSql(showSql);
//        adapter.setGenerateDdl(true);
//        return adapter;
//    }
//
//    @Bean
//    public JpaTransactionManager transactionManager() {
//        final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
//        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//        return jpaTransactionManager;
//    }
//    @Bean
//    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){
//        return new JPAQueryFactory(entityManager);
//    }
//}
// spring-mvc end <<<<