package com.support.aop;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;


/**
 * 记录用户请求及响应数据日志
 *
 * @author 谢长春 2017-9-29
 */
public interface IControllerAspect {
    Logger log = LoggerFactory.getLogger(IControllerAspect.class);
    String START = Strings.repeat("┬", 200);
    String END = Strings.repeat("┴", 200);

    default void log(final JoinPoint joinPoint, final Object result, final LocalTime time) {
        if (result instanceof SseEmitter) {
            log.info("SseEmitter");
            return;
        }
        log.info("\n{}\n{}\ntime:{}ms({}-{})\nurl:{}\nargs:{}\nresult:{}\n{}",
                START,
                joinPoint,
                Duration.between(time, LocalTime.now()).toMillis(),
                time,
                LocalTime.now(),
                getPath(),
                Arrays.toString(joinPoint.getArgs()),
                Objects.isNull(result) ? "" : JSON.toJSONString(result),
                END
        );
    }

    /**
     * 获得请求URL
     *
     * @return String
     */
    default String getPath() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRequestURI();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "请求 URL 获取失败";
        }
    }
}
