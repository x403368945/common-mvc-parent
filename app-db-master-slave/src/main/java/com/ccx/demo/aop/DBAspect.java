package com.ccx.demo.aop;

import com.ccx.demo.tl.DBContext;
import com.support.aop.annotations.Master;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <pre>
 * 控制数据源切换策略，可以使用 @{@link Master} 注解强制操作主库
 * 数据库上下文切换拦截策略
 *   Service 拦截缺陷：
 *     AService.getA 方法调用 BService.getB 方法，如果只有两个从库，那每次执行时，都会切换两次数据库上下文，如果没有其他方法干扰，会绝对落在第二个从库上
 *     AService.saveA 方法调用 BService.getB 方法，再调用 ARepository.save 方法，会出现 ARepository.save 向从库中插入数据
 *   DAO 拦截缺陷：
 *     数据库上下文切换频率高，每一个 DAO 方法都会切换一次数据源，同一个 Service.find() 方法多次调用同一个 Repository.find() 方法，会切换多次上下文
 *     无法做到在同一个事务中，查询未提交的数据。因为查询会落在从库，但主库事务未提交，所以无法查询
 *   建议拦截策略放在 DAO 层，控制粒度更细，适配性更高
 * @author 谢长春 2019/6/9
 */
@Component
@Aspect
@Slf4j
//@Order(-1) // 需要保证事务切面的 order 值要大于数据源切面，order 值越小权重越高
public class DBAspect {
//    /**
//     * 主库操作拦截
//     */
//    @Around("execution(* com.ccx..*.service..*.save*(..)) " +
//            "||execution(* com.ccx..*.service..*.update*(..)) " +
//            "||execution(* com.ccx..*.service..*.markDelete*(..)) " +
//            "||execution(* com.ccx..*.service..*.delete*(..)) " +
//
//            "||@annotation(com.support.aop.annotations.Master) " +
//            "||execution(* com.ccx..*.service..*.add*(..)) " +
//            "||execution(* com.ccx..*.service..*.insert*(..)) " +
//            "||execution(* com.ccx..*.service..*.edit*(..)) " +
//            "||execution(* com.ccx..*.service..*.remove*(..)) "
//    )
//    @Around("execution(* com.ccx..*.dao.jpa..*.save*(..))" +
//            "||execution(* com.ccx..*.dao.jpa..*.update*(..))" +
//            "||execution(* com.ccx..*.dao.jpa..*.markDelete*(..))" +
//            "||execution(* com.ccx..*.dao.jpa..*.delete*(..))" +
//
//            "||@annotation(com.support.aop.annotations.Master)" +
//            "||execution(* com.ccx..*.dao.jpa..*.add*(..))" +
//            "||execution(* com.ccx..*.dao.jpa..*.insert*(..))" +
//            "||execution(* com.ccx..*.dao.jpa..*.edit*(..))" +
//            "||execution(* com.ccx..*.dao.jpa..*.remove*(..))"
//    )
//    @SneakyThrows
//    public Object masterAround(final ProceedingJoinPoint joinPoint) {
//        try {
//            DBContext.master();
////            log.debug("master {}", joinPoint.getSignature().toLongString());
//            return joinPoint.proceed();
//        } finally {
//            DBContext.remove(); // 方法执行完之后，移除 ThreadLocal ，避免内存泄露
//        }
//    }

    /**
     * 从库操作拦截
     */
    @Around("execution(* com.ccx..*.service..*.find*(..)) " +
            "||execution(* com.ccx..*.service..*.get*(..)) " +
            "||execution(* com.ccx..*.service..*.search*(..)) " +
            "||execution(* com.ccx..*.service..*.exist*(..)) " +
            "||execution(* com.ccx..*.service..*.load*(..)) " +
            "||execution(* com.ccx..*.service..*.count*(..)) "
    )
//    @Around("execution(* com.ccx..*.dao.jpa..*.find*(..)) && !@annotation(com.support.aop.annotations.Master)")
    @SneakyThrows
    public Object slaveAround(final ProceedingJoinPoint joinPoint) {
        try {
            // 经测试 !@annotation(com.support.aop.annotations.Master) 不生效，
            // 但是 @annotation(com.support.aop.annotations.Master) 生效，
            // 所以这里只能用代码判断，符合 execution 表达式且不带 @Master 注解的方法才切换从库
            // 后面再找不生效原因
            if (Objects.isNull(((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Master.class))) {
                DBContext.slave();
            }
//            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//            Method method1 = joinPoint.getTarget().getClass().getMethod(joinPoint.getSignature().getName(), Stream.of(joinPoint.getArgs()).map(Object::getClass).toArray(Class[]::new));
//            log.debug("slave {}\n{}\n{}\n{}\n{}\n{}\n{}\n{}\n{}\n{}\n{}", joinPoint.getSignature().toLongString(),
//                    method.getAnnotations(),
//                    method.getAnnotation(Master.class),
//                    method.getAnnotationsByType(Master.class),
//                    method.getDeclaredAnnotation(Master.class),
//                    method.getDeclaredAnnotationsByType(Master.class),
//                    method1.getAnnotations(),
//                    method1.getAnnotation(Master.class),
//                    method1.getAnnotationsByType(Master.class),
//                    method1.getDeclaredAnnotation(Master.class),
//                    method1.getDeclaredAnnotationsByType(Master.class)
//            );
            return joinPoint.proceed();
        } finally {
            DBContext.remove(); // 方法执行完之后，移除 ThreadLocal ，避免内存泄露
        }
    }

//    /**
//     * 方法执行完之后，移除 ThreadLocal ，避免内存泄露
//     */
//    @After("execution(* com.ccx..*.dao.jpa..*.*(..))")
//    public void clear() {
//        DBContext.remove();
//    }
}
