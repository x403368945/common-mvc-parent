package com.config;

/**
 * 动作行为组件接口：初始化配置 <br>
 * 可以通过 applicationContext.getBeansOfType(Initializer.class) ； 获取有所实现 Initializer 接口的类 <br>
 * applicationContext.getBeansOfType(Initializer.class)
 * .values()
 * .stream()
 * .sorted(Comparator.comparing(Initializer::priority))
 * .forEach(Initializer::init);
 *
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