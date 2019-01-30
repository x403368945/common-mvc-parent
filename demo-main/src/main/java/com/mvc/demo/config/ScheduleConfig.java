package com.mvc.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 定时任务配置
 *
 *
 * @author 谢长春 on 2018/1/5.
 */
@Configuration
@EnableScheduling
@ComponentScan(basePackages = "com.mvc.**.task")
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(scheduler());
    }

    /**
     * 方案一：支持 CronTrigger 表达式
     * 多线程定时任务调度器
     * @return ThreadPoolTaskScheduler
     */
    @Bean(destroyMethod = "destroy")
    public ThreadPoolTaskScheduler scheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20);
        return scheduler;
    }

//    /**
//     * 方案二：仅支持时间戳
//     * 多线程定时任务调度器
//     * @return ScheduledExecutorService
//     */
//    @Bean(destroyMethod = "shutdownNow")
//    public ScheduledExecutorService scheduler() {
//        return Executors.newScheduledThreadPool(10);
//    }
}