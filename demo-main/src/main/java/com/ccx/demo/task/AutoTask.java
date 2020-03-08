package com.ccx.demo.task;

import com.ccx.demo.business.common.service.AutoTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务，暂未开启定义任务，若需要定时任务请配置@EnableScheduling
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * >> 由于Spring scheduling包并不是依赖quartz库，表达式虽然跟quartz类似，但部分quartz表达式在这里并不支持，例如每月最后一天 L，Spring Scheduled 不支持
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 *
 * @author 谢长春 2016-11-23
 */
@Component
@Slf4j
@ConditionalOnProperty(value = "app.auto-task.enabled", havingValue = "true")
public class AutoTask {
    @Autowired
    private AutoTaskService autoTaskService;
//	/**
//	 * 测试定时任务
//	 */
//	@Scheduled(cron = "0/10 * * * * ?")
//	public void test(){
//		log.error("测试定时任务");
//	}

    /**
     * 触发时间：每30分钟
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void minute30() {
        try {
            MDC.put("rid", RandomStringUtils.randomAlphanumeric(8));
            log.info("开始:");
            autoTaskService.clearTempDirectory();
        } catch (Exception e) {
            log.error("异常:", e);
        } finally {
            log.info("结束:");
            MDC.clear();
        }
    }

    /**
     * 触发时间：每月1号凌晨0点
     * > 清除临时目录
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void month() {
        try {
            MDC.put("rid", RandomStringUtils.randomAlphanumeric(8));
            log.info("开始:清除临时文件目录");
            autoTaskService.clearTempDirectory();
        } catch (Exception e) {
            log.error("异常:清除临时文件目录", e);
        } finally {
            log.info("结束:清除临时文件目录");
            MDC.clear();
        }
    }

//
//	/**
//	 * 触发时间：每月1号0:15
// 	 */
////	@Scheduled(cron = "0 15 0 1 * ?")
//	@Scheduled(cron = "0 15 0 1 * ?")
//	public void report(){
//	}

}
