package com.ccx.demo.aop;

import com.ccx.demo.business.user.entity.TabUser;
import com.support.aop.IServiceAspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * <pre>
 * {@link IServiceAspect}
 * AOP所有的拦截和@Transactional注解的事务，在类内部调用方法之间相互调用时不会起作用
 * 比如：在 saveAll 方法中调用 save 方法，则save方法的事务和AOP都不会起作用
 * 比如：在 udpate 方法中调用 delete 方法，则 delete 方法的事务和AOP都不会起作用，
 *       这样 {@link ServiceAspect#afterDelete} 就无法记录删除的数据，所以不建议在 service 内部调用 delete 方法
 *
 *
 * @author 谢长春 2018-10-4
 */
@Component
@Aspect
@Slf4j
public class ServiceAspect implements IServiceAspect {
    /**
     * <pre>
     * TODO 这里小心入坑
     * 如果 service 在子线程中，将获取不到用户信息，因为 Spring Security 用户信息放在 ThreadLocal 中
     * 如果在子线程中调用 service 中的方法，且该方法必须要有用户名，需要子线程外先设置好用户姓名，假如用户信息有放在缓存中，也可以通过用户ID获取缓存用户的信息
     */
    @Override
    public String getNickname() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .map(o -> o instanceof TabUser ? ((TabUser) o).getNickname() : null)
                .orElse(null)
                ;
    }

    @Before("execution(* com.ccx..*.service..*.save(..))")
    public void beforeSave(final JoinPoint joinPoint) {
        save(joinPoint);
    }

    @Before("execution(* com.ccx..*.service..*.saveAll(..))")
    public void beforeSaveAll(final JoinPoint joinPoint) {
        saveAll(joinPoint);
    }

    @Before("execution(* com.ccx..*.service..*.update(..))")
    public void beforeUpdate(final JoinPoint joinPoint) {
        update(joinPoint);
    }

    @AfterReturning(pointcut = "execution(* com.ccx..*.service..*.delete*(..))", returning = "result")
    public void afterDelete(final JoinPoint joinPoint, final Object result) {
        delete(joinPoint, result);
    }
}
