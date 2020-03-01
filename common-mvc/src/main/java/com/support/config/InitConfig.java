package com.support.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Comparator;

/**
 * <pre>
 * 初始化配置：需要在WebMvcConfig中引入配置 @Import(value = {InitConfig.class})
 * {@link ApplicationListener}该接口的作用是在Spring启动的最后阶段，Spring自动执行 {@link ApplicationListener#onApplicationEvent}
 * 可以通过 applicationContext.getBeansOfType(Initializer.class) ； 获取有所实现 Initializer 接口的类
 * applicationContext.getBeansOfType(Initializer.class)
 * .values()
 * .stream()
 * .sorted(Comparator.comparing(Initializer::priority))
 * .forEach(Initializer::init);
 *
 * @author 谢长春 on 2017/11/14.
 */
@RequiredArgsConstructor
public class InitConfig implements ApplicationListener<ContextRefreshedEvent> {
    private final ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        // 初始化所有实现 Initializer 接口，且注解为 @Component 的类
        applicationContext.getBeansOfType(Initializer.class)
                .values()
                .stream()
                .sorted(Comparator.comparing(Initializer::priority))
                .forEach(Initializer::init);
    }

    /**
     * 初始化配置 <br>
     * 可以通过 applicationContext.getBeansOfType(Initializer.class) ； 获取有所实现 Initializer 接口的类 <br>
     * applicationContext.getBeansOfType(Initializer.class)
     * .values()
     * .stream()
     * .sorted(Comparator.comparing(Initializer::priority))
     * .forEach(Initializer::init);
     *
     * @author 谢长春 on 2017/11/14.
     */
    public interface Initializer {
        /**
         * 优先级，初始化时将会使用优先级排序，0 优先级最高
         *
         * @return int
         */
        default int priority() {
            return 9999;
        }

        /**
         * 初始化
         */
        void init();
    }
}