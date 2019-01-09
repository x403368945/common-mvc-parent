package com.demo.aop;

import com.aop.IControllerAspect;
import com.demo.config.init.AppConfig;
import com.mvc.entity.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Optional;


/**
 * 记录用户请求及响应数据日志
 *
 *
 * @author 谢长春 2017-9-29
 */
@Component
@Aspect
@Slf4j
public class ControllerAspect implements IControllerAspect {

    /**
     * 添加业务逻辑方法切入点
     */
    @Pointcut(value = "execution(* com.demo.open.*.web..*.*(..))")
    public void open() {
    }

    /**
     * 添加业务逻辑方法切入点
     */
    @Pointcut(value = "execution(* com.demo.business.*.web..*.*(..))")
    public void business() {
    }

    /**
     * 添加业务逻辑方法切入点
     */
    @Pointcut("open()||business()")
    public void point() {
    }

    private LocalTime time;
    @Before(value = "point()")
    public void before() {
        time = LocalTime.now();
    }

//    @After(value = "point()")
//    public void after(JoinPoint joinPoint) {
//
//    }

    @AfterReturning(value = "point()", returning = "result")
    public void afterReturn(JoinPoint joinPoint, Object result) {
        log(joinPoint, result, time);
        if (AppConfig.isDev() && result instanceof Result) {
            Optional.ofNullable(((Result) result).getVersion())
                    .ifPresent(version -> version.write(AppConfig.Path.MD.absolute()));
        } else if (AppConfig.isProd()) {
            // 生产环境不返回最详细的版本信息
            Optional.ofNullable(((Result) result)).ifPresent(r -> r.setVersion(null));
        }
    }
}
