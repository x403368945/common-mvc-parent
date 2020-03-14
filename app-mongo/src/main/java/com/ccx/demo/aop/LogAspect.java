package com.ccx.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 拦截所有 service 、 dao 层方法入参打印到日志
 *
 * @author 谢长春 2018-10-4
 */
@Component
@Aspect
@Slf4j
@Order(9999)
public class LogAspect {

    @Before("execution(* com.ccx..*.service..*.*(..))" +
            "||execution(* com.ccx..*.dao..*.*(..))")
    public void before(final JoinPoint joinPoint) {
        log.info("{} => {}", joinPoint, Arrays.toString(joinPoint.getArgs()));
    }
}
