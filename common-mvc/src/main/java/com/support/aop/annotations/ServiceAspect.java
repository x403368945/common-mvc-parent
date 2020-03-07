package com.support.aop.annotations;

import com.support.mvc.service.IBaseService;
import com.support.mvc.service.IOpenService;

import java.lang.annotation.*;

/**
 * <pre>
 * {@link IBaseService}
 * {@link IOpenService}
 * 接口的实现类，save|saveAll|update 方法配置拦截器
 * 该注解支持类和方法上加，方法上的注解优先；一般配置的类头部即可
 * Service 会有代理方法，自动设置 UUID 和 操作用户信息，部分业务场景需要将数据库表同步到 mongo ，需要保留数据库的 ID 和时间戳；
 * 所以通过该注解配置自动装配信息
 *
 *
 * @author 谢长春 2018-10-11
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceAspect {
    /**
     * <pre>
     * 该 service 是否为同步镜像服务；比如将 mysql 数据以镜像形式存储到其他数据库；
     * true：同步镜像服务
     * false : 非同步镜像服务
     * 当值为 true 时，忽略以下配置
     *  {@link ServiceAspect#id()}
     *  {@link ServiceAspect#uid()}
     *  {@link ServiceAspect#user()}
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
     * save | saveAll 方法是否自动设置 uid
     *
     * @return boolean
     */
    boolean uid() default true;

    /**
     * save | saveAll 方法是否自动设置用户 id 到 [insertUserId|updateUserId] 字段
     * update 方法是否自动设置用户 id 到 updateUserId 字段
     *
     * @return boolean
     */
    boolean user() default true;

}
