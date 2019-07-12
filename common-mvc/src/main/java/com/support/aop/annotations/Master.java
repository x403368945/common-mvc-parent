package com.support.aop.annotations;

import java.lang.annotation.*;

/**
 * 自定义注解：强制方法读写主库；可以通过该注解，跳出默认策略之外，强制指定读写主库
 *
 * @author 谢长春 2019/6/9
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Master {
}
