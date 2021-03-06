package com.support.aop;

import com.alibaba.fastjson.JSON;
import com.support.aop.annotations.MongoServiceAspect;
import com.support.aop.annotations.ServiceAspect;
import com.utils.util.Util;
import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Service 方法拦截器
 * <pre>
 * {@link com.support.mvc.service.IBaseService#save(Object, Long)}
 * {@link com.support.mvc.service.IOpenService#save(Object)}
 * {@link com.support.mvc.service.IBaseService#save(Object, Long)}
 * {@link com.support.mvc.service.IOpenService#save(Object)}
 *
 * {@link com.support.mvc.service.IBaseService#saveAll(List, Long)}
 * {@link com.support.mvc.service.IOpenService#saveAll(List)}
 * {@link com.support.mvc.service.IBaseService#saveAll(List, Long)}
 * {@link com.support.mvc.service.IOpenService#saveAll(List)}
 *
 * {@link com.support.mvc.service.IBaseService#update(Long, Long, Object)}
 * {@link com.support.mvc.service.IOpenService#update(Long, Object)}
 * {@link com.support.mvc.service.IBaseService#update(Long, Long, Object)}
 * {@link com.support.mvc.service.IOpenService#update(Long, Object)}
 *
 * {@link com.support.mvc.service.IBaseService#deleteById(Long, Long)}
 * {@link com.support.mvc.service.IBaseService#deleteByUid(Long, String, Long)}
 * {@link com.support.mvc.service.IOpenService#deleteById(Long)}
 * {@link com.support.mvc.service.IOpenService#deleteByUid(Long, String)}
 * {@link com.support.mvc.service.IBaseService#deleteById(Long, Long)}
 * {@link com.support.mvc.service.IBaseService#deleteByUid(Long, String, Long)}
 * {@link com.support.mvc.service.IOpenService#deleteById(Long)}
 * {@link com.support.mvc.service.IOpenService#deleteByUid(Long, String)}
 *
 *
 * @author 谢长春 2018-10-4
 */
//@Component
//@Aspect
public interface IServiceAspect {
    Logger log = LoggerFactory.getLogger(IServiceAspect.class);

    /**
     * Spring Security 中的用户信息不能在子线程中访问，如果 service 在子线程，则无法获取到会话中的用户信息
     *
     * @return String
     */
    default String getNickname() {
        return null;
    }

    //    @Before(value = "point()")
    default void save(final JoinPoint joinPoint) {
//        log.info("{}", ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ServiceAspect.class));
//        log.info("{}", joinPoint.getTarget().getClass().getAnnotation(ServiceAspect.class));
//        log.info("{}", Optional.ofNullable(joinPoint.getArgs()).map(JSON::toJSONString).orElse("参数为空"));
        final ServiceAspect mysql = Optional.ofNullable(((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ServiceAspect.class)) // 方法头注解
                .orElseGet(() -> joinPoint.getTarget().getClass().getAnnotation(ServiceAspect.class)); // 当方法没有添加注解时从类头部获取
        if (Objects.nonNull(mysql)) { // 没有添加 @ServiceAspect 注解的直接跳出
            if (!mysql.sync()) { // 标记为同步状态锁定的直接跳出
                final Object obj = joinPoint.getArgs()[0]; // 当前新增对象
                final Object userId = joinPoint.getArgs().length > 1 ? joinPoint.getArgs()[1] : null; // 当前操作用户id
                Setter.beanSaveMysql(mysql, obj, userId, getNickname());
            }
        } else { // mongo
            final MongoServiceAspect mongo = Optional.ofNullable(((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(MongoServiceAspect.class)) // 方法头注解
                    .orElseGet(() -> joinPoint.getTarget().getClass().getAnnotation(MongoServiceAspect.class)); // 当方法没有添加注解时从类头部获取
            if (Objects.nonNull(mongo)) { // 没有添加 @ServiceAspect 注解的直接跳出
                if (!mongo.sync()) { // 标记为同步状态锁定的直接跳出
                    final Object obj = joinPoint.getArgs()[0]; // 当前新增对象
                    final Object userId = joinPoint.getArgs().length > 1 ? joinPoint.getArgs()[1] : null; // 当前操作用户id
                    Setter.beanSaveMongo(mongo, obj, userId, getNickname());
                }
            }
        }

    }

    //    @Before(value = "point()")
    default void saveAll(final JoinPoint joinPoint) {
//        log.info("{}", ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ServiceAspect.class));
//        log.info("{}", joinPoint.getTarget().getClass().getAnnotation(ServiceAspect.class));
//        log.info("{}", Optional.ofNullable(joinPoint.getArgs()).map(JSON::toJSONString).orElse("参数为空"));
        final ServiceAspect mysql = Optional.ofNullable(((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ServiceAspect.class)) // 方法头注解
                .orElseGet(() -> joinPoint.getTarget().getClass().getAnnotation(ServiceAspect.class)); // 当方法没有添加注解时从类头部获取
        if (Objects.nonNull(mysql)) { // 没有添加 @ServiceAspect 注解的直接跳出
            if (!mysql.sync()) { // 标记为同步状态锁定的直接跳出
                final List<?> list = (List<?>) joinPoint.getArgs()[0]; // 当前新增对象集合
                final Object userId = joinPoint.getArgs().length > 1 ? joinPoint.getArgs()[1] : null; // 当前操作用户id
                if (Objects.nonNull(list)) {
                    final String nickname = getNickname();
                    list.forEach(obj -> Setter.beanSaveMysql(mysql, obj, userId, nickname));
                }
            }
        } else { // mongo
            final MongoServiceAspect mongo = Optional.ofNullable(((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(MongoServiceAspect.class)) // 方法头注解
                    .orElseGet(() -> joinPoint.getTarget().getClass().getAnnotation(MongoServiceAspect.class)); // 当方法没有添加注解时从类头部获取
            if (Objects.nonNull(mongo)) { // 没有添加 @ServiceAspect 注解的直接跳出
                if (!mongo.sync()) { // 标记为同步状态锁定的直接跳出
                    final List<?> list = (List<?>) joinPoint.getArgs()[0]; // 当前新增对象集合
                    final Object userId = joinPoint.getArgs().length > 1 ? joinPoint.getArgs()[1] : null; // 当前操作用户id
                    if (Objects.nonNull(list)) {
                        final String nickname = getNickname();
                        list.forEach(obj -> Setter.beanSaveMongo(mongo, obj, userId, nickname));
                    }
                }
            }
        }
    }


    //    @Before(value = "point()")
    default void update(final JoinPoint joinPoint) {
        //        log.info("{}", ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ServiceAspect.class));
//        log.info("{}", joinPoint.getTarget().getClass().getAnnotation(ServiceAspect.class));
//        log.info("{}", Optional.ofNullable(joinPoint.getArgs()).map(JSON::toJSONString).orElse("参数为空"));
        // mysql
        final ServiceAspect mysql = Optional.ofNullable(((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ServiceAspect.class)) // 方法头注解
                .orElseGet(() -> joinPoint.getTarget().getClass().getAnnotation(ServiceAspect.class)); // 当方法没有添加注解时从类头部获取
        if (Objects.nonNull(mysql)) { // 没有添加 @ServiceAspect 注解的直接跳出
            if (!mysql.sync()) { // 标记为同步状态锁定的直接跳出，表示该方法是将 A 表同步到 B 表，不能重新生成id
                final int len = joinPoint.getArgs().length;
                final Object id = joinPoint.getArgs()[0]; // 当前操作用户id
                final Object userId = len == 3 ? joinPoint.getArgs()[1] : null; // 当前操作用户id
                final Object obj = joinPoint.getArgs()[len == 3 ? 2 : 1]; // 当前新增对象
                Setter.beanUpdateMysql(mysql, id, userId, obj, getNickname());
            }
        } else { // mongo
            final MongoServiceAspect mongo = Optional.ofNullable(((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(MongoServiceAspect.class)) // 方法头注解
                    .orElseGet(() -> joinPoint.getTarget().getClass().getAnnotation(MongoServiceAspect.class)); // 当方法没有添加注解时从类头部获取
            if (Objects.nonNull(mongo)) { // 没有添加 @ServiceAspect 注解的直接跳出
                if (!mongo.sync()) { // 标记为同步状态锁定的直接跳出，表示该方法是将 A 表同步到 B 表，不能重新生成id
                    final int len = joinPoint.getArgs().length;
                    final Object id = joinPoint.getArgs()[0]; // 当前操作用户id
                    final Object userId = len == 3 ? joinPoint.getArgs()[1] : null; // 当前操作用户id
                    final Object obj = joinPoint.getArgs()[len == 3 ? 2 : 1]; // 当前新增对象
                    Setter.beanUpdateMongo(mongo, id, userId, obj, getNickname());
                }
            }
        }
    }

    //    @AfterReturning(value = "point()", returning = "result")
    default void delete(final JoinPoint joinPoint, final Object result) {
        log.info(String.join("\n",
                ">>>>>>>>> 物理删除操作",
                String.valueOf(joinPoint),
                "args:".concat(Arrays.toString(joinPoint.getArgs())),
                "result:".concat(JSON.toJSONString(result))
        ));
    }

    /**
     * @author 谢长春 2018/12/20
     */
    class Setter {
        private static void set(final Object bean, final String name, final Object value) {
            try {
//                BeanUtils.setProperty(bean, name, value);
                PropertyUtils.setProperty(bean, name, value);
            } catch (Exception e) {
                // 不显示异常
            }
        }

        private static void beanSaveMysql(final ServiceAspect service, final Object obj, final Object userId, final String nickname) {
            if (service.id()) { // 将新增对象中的 id 置空
                set(obj, "id", null);
            }
            if (service.uid()) { // 生成 uuid 填充到新增对象 uid 字段
                set(obj, "uid", Util.uuid32());
            }
            if (service.user()) { // 将当前操作用户填充到新增对象 insertUserId|updateUserId 字段
                set(obj, "insertUserId", userId);
                set(obj, "updateUserId", userId);
                if (Objects.nonNull(nickname)) {
                    set(obj, "insertUserName", nickname);
                    set(obj, "updateUserName", nickname);
                }
            }
        }

        private static void beanUpdateMysql(final ServiceAspect service, final Object id, final Object userId, final Object obj, final String nickname) {
            if (service.id()) { // 指定要更新的数据ID
                set(obj, "id", id);
            }
            if (service.user()) { // 将当前操作用户填充到编辑对象 updateUserId 字段
                set(obj, "updateUserId", userId);
                if (Objects.nonNull(nickname)) {
                    set(obj, "updateUserName", nickname);
                }
            }
        }

        @SuppressWarnings({"unchecked"})
        private static void beanSaveMongo(final MongoServiceAspect service, final Object obj, final Object userId, final String nickname) {
            if (service.id()) { // mongodb 不支持自增，设置为uuid
                set(obj, "id", Util.uuid32());
            }
            if (service.user()) { // 将当前操作用户填充到新增对象 insertUserId|updateUserId 字段
                set(obj, "insertUserId", userId);
                set(obj, "updateUserId", userId);
                if (Objects.nonNull(nickname)) {
                    set(obj, "insertUserName", nickname);
                    set(obj, "updateUserName", nickname);
                }
            }
            if (service.timestamp()) { // MongoDB 没有自动更新时间戳的功能，将当前时间戳填充到新增对象 createTime|modifyTime 字段
                set(obj, "insertTime", Timestamp.from(Instant.now()));
            }
            // 填充 Bool.NO 到新增对象 deleted 字段
            set(obj, "deleted", Enum.valueOf(service.deleted(), "NO"));  // 只有 mongodb 才需要，mysql 可以在建表的时候设置默认值
        }

        private static void beanUpdateMongo(final MongoServiceAspect service, final Object id, final Object userId, final Object obj, final String nickname) {
            if (service.id()) { // 指定要更新的数据ID
                set(obj, "id", id);
            }
            if (service.user()) { // 将当前操作用户填充到编辑对象 updateUserId 字段
                set(obj, "updateUserId", userId);
                if (Objects.nonNull(nickname)) {
                    set(obj, "updateUserName", nickname);
                }
            }
        }
    }

}
