package com.demo.config;

/**
 * Spring Data MongoDB 配置
 * AbstractMongoConfiguration 已经默认实现了 mongoTemplate和mongoFactoryBean
 * 参考配置：https://docs.spring.io/spring-data/mongodb/docs/2.0.0.RC3/reference/html/
 * QueryDSL 参考配置： http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/#mongodb_integration
 *
 * @author 谢长春
 */
//@Configuration
//@EnableMongoRepositories(basePackages = {"com.demo.**.dao.mongo"})
////,repositoryFactoryBeanClass= CustomMongoRepositoryFactoryBean.class
//public class MongodbConfig extends AbstractMongoConfiguration {
//
//    @Value("${mongo.host}")
//    private String host;
//    @Value("${mongo.port}")
//    private Integer port;
//    @Value("${mongo.database}")
//    private String database;
//
//    @Override
//    protected String getDatabaseName() {
//        return database;
//    }
//
//    @Bean
//    @Override
//    public CustomConversions customConversions() {
//        return new MongoCustomConversions(Collections.singletonList(
//                new TimestampConverter()
////                new TimestampWriteConverter(),
////                new TimestampReadConverter(),
////                new DateWriteConverter(),
////                new DateReadConverter()
//        ));
//    }
//
//    class TimestampConverter implements Converter<Date, Timestamp> {
//        @Override
//        public Timestamp convert(Date date) {
//            return new Timestamp(date.getTime());
//        }
//    }
//
////    class TimestampWriteConverter implements Converter<Timestamp, String> {
////        @Nullable
////        @Override
////        public String convert(Timestamp timestamp) {
////            return Dates.of(timestamp).format(yyyyMMddHHmmssSSS);
////        }
////    }
////    class TimestampReadConverter implements Converter<String, Timestamp> {
////        @Nullable
////        @Override
////        public Timestamp convert(String s) {
////            return Dates.of(s, yyyyMMddHHmmssSSS).timestamp();
////        }
////    }
////    class DateWriteConverter implements Converter<Date, String> {
////        @Nullable
////        @Override
////        public String convert(Date date) {
////            return Dates.of(date).format(yyyyMMddHHmmssSSS);
////        }
////    }
////    class DateReadConverter implements Converter<String, Date> {
////        @Nullable
////        @Override
////        public Date convert(String s) {
////            return Dates.of(s, yyyyMMddHHmmssSSS).date();
////        }
////    }
//
//    @Bean
//    @Override
//    public MongoClient mongoClient() {
//        return new MongoClient(
//                new ServerAddress(host, port)
////                , MongoCredential.createCredential("name", "jpa", "pwd".toCharArray())
//        );
//    }
//
//    //    @Bean
////    public MongoTypeMapper customTypeMapper() {
////        return new DefaultMongoTypeMapper(null); // typeKey为null的时候，插入mongodb 不会产生 _class 属性
////    }
//    @Bean
//    @Override
//    public MappingMongoConverter mappingMongoConverter() throws Exception {
//        final MappingMongoConverter converter = super.mappingMongoConverter();
//        converter.setTypeMapper(new DefaultMongoTypeMapper(null)); // typeKey为null的时候，插入mongodb 不会产生 _class 属性
//        return converter;
//
//    }
//
//    @Bean
//    public Morphia morphiaMongo() {
//        return new Morphia();
//    }
//
//    @Bean
//    public Datastore morphiaDatastore(@Autowired Morphia morphiaMongo, @Autowired MongoClient mongoClient) {
//        return morphiaMongo.createDatastore(mongoClient, database);
//    }
//
////    /**
////     * QueryDSL 查询对象
////     * @return MorphiaQuery<ActivityData>
////     */
////    public MorphiaQuery<ActivityData> morphiaQuery(){
////        final QActivityData q = QActivityData.activityData;
////        return new MorphiaQuery<ActivityData>(
////                morphiaMongo(),
////                morphiaDatastore(),
////                q
////        ).where(q.activityId.eq(""));
////    }
//}