package com.ccx.socket.config.init;

import com.support.config.InitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/**
 * 初始化单例类、实体类、接口需要的bean，因为单例类无法直接注入bean
 *
 * @author 谢长春
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BeanInitializer implements InitConfig.Initializer {
    private static ApplicationContext APP_CONTEXT;
    private final ApplicationContext applicationContext;

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public void init() {
        APP_CONTEXT = this.applicationContext;
    }


    /**
     * TODO 枚举：定义单例类、实体类、接口需要的bean，因为单例类无法直接注入bean
     */
    public enum Beans {
        //        jedisPool("redis 缓存池", JedisPool.class),
        singleThread("单线程服务", ExecutorService.class),
        multiThread("多线程服务", ExecutorService.class),
//        jpaQueryFactory("QueryDSL 数据操作，通过此枚举获取到 jpa 查询对象，可以在接口中声明 default 方法后做更新删除查询操作", JPAQueryFactory.class),
//        mongoTemplate("Mongodb 数据操作，通过此枚举获取到 mongo 查询对象，可以在接口中声明 default 方法后做更新删除查询操作", MongoTemplate.class),
        ;
        // 枚举属性说明
        public final String comment;
        private final Supplier<?> supplier;

        Beans(final String comment, final Class<?> clazz) {
            this.comment = comment;
            supplier = () -> APP_CONTEXT.getBean(this.name(), clazz);
        }

        /**
         * 获得 bean，示例：
         * <pre>
         *     Beans.getAppContext().getBean("userService", UserService.class)
         *     Beans.jpaQueryFactory.<JPAQueryFactory>get().selectFrom(QTabUser.tabUser).fetch()
         *
         * @param <T> 泛型类型
         * @return T
         */
        @SuppressWarnings("unchecked")
        public <T> T get() {
            return (T) supplier.get();
        }
    }

    /**
     * 获取 Spring Context 对象
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getAppContext() {
        return APP_CONTEXT;
    }

}