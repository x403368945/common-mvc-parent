package com.aop.annotations;

import java.lang.annotation.*;

/**
 * @author 谢长春 2019/1/4
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoServiceAspect {
    enum Deleted {
        NO, YES
    }

    /**
     * <pre>
     * 该 service 是否为同步镜像服务；比如将 mongodb 数据以镜像形式存储到其他数据库；
     * true：同步镜像服务
     * false : 非同步镜像服务
     *
     * 当值为 true 时，忽略以下配置
     *   {@link MongoServiceAspect#id()}
     *   {@link MongoServiceAspect#user()}
     *   {@link MongoServiceAspect#timestamp()}
     * @return boolean
     */
    boolean sync() default false;

    /**
     * save | saveAll 方法是否自动清空ID
     * update 方法是否自动设置参数中的 ID 到更新对象中
     *
     * @return boolean
     */
    boolean id() default true;

    /**
     * save | saveAll 方法是否自动设置用户 id 到 [createUserId|modifyUserId] 字段
     * update 方法是否自动设置用户 id 到 modifyUserId 字段
     *
     * @return boolean
     */
    boolean user() default true;

    /**
     * save | saveAll 方法是否自动设置Radio.NO 到 deleted 字段
     */
    Class<? extends Enum> deleted() default Deleted.class;

    /**
     * save | saveAll 方法是否自动设置当前时间到 [createTime|modifyTime] 字段
     * update 方法是否自动设置当前时间到 modifyTime 字段
     *
     * @return boolean
     */
    boolean timestamp() default true;
}
