package com.config;

import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.annotation.*;
import java.util.stream.Collectors;

/**
 * <pre>
 * 初始化 EventBus：需要在WebMvcConfig中引入配置 @Import(value = {BusConfig.class})
 * 所有注解 @{@link EventBusListener} 的服务都将注册到全局广播事件监听
 * {@link ApplicationListener}该接口的作用是在Spring启动的最后阶段，Spring自动执行 {@link ApplicationListener#onApplicationEvent}
 *
 *
 * @author 谢长春 on 2017/11/14.
 */
@Slf4j
public class BusConfig implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ApplicationContext applicationContext;

    private static final EventBus EVENT_BUS = new EventBus();

    @Bean
    public EventBus eventBus() {
        return EVENT_BUS;
    }

//    @Override
//    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
//        if (beanName.equals("demoListService")) {
//            log.debug(bean.getClass().getName());
//        }
//        Optional.ofNullable(bean.getClass().getAnnotation(EventBusListener.class))
//                .ifPresent(listener -> {
//                    EVENT_BUS.register(listener);
//                });
//        return bean;
//    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        log.info("┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬ 注册 EVENT_BUS 广播监听 ┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬┬");
        log.info(applicationContext.getBeansWithAnnotation(EventBusListener.class).entrySet().stream()
                        .map(entry -> {
//                    System.out.println(JSON.toJSONString(Arrays.asList(entry.getKey(), entry.getValue().getClass().getName())));
                            EVENT_BUS.register(entry.getValue());
                            return String.format("%s:%s", entry.getKey(), entry.getValue().getClass().getName());
                        })
                        .collect(Collectors.joining("\n"))
        );
        log.info("┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴ 注册 EVENT_BUS 广播监听 ┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴┴");

    }

    /**
     * EventBus 广播事件监听注解，所有使用 @{@link EventBusListener} 注解的服务，都将会被注册到 {@link BusConfig#EVENT_BUS}
     */
    @Documented
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface EventBusListener {
    }
}