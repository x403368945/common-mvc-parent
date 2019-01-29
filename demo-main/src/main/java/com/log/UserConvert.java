package com.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.boot.demo.config.interceptor.LogUserInterceptor;

/**
 * 将当前操作用户写入日志
 *
 *
 * @author 谢长春 2018-10-5
 */
public class UserConvert extends ClassicConverter {

    @Override
    public String convert(final ILoggingEvent event) {
        return LogUserInterceptor.get();
//        return Optional
//                .ofNullable(SecurityContextHolder.getContext().getAuthentication())
//                .map(Authentication::getPrincipal)
//                .map(principal -> {
//                    if (principal instanceof TabUser) {
//                        final TabUser user = (TabUser) principal;
//                        return String.format("%d:%s", user.getId(), user.getUsername());
//                    }
//                    return Objects.toString(principal);
//                })
//                .orElse("");
    }
}
