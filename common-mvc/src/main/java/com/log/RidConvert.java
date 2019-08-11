package com.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 将当前请求标记写入日志
 *
 * @author 谢长春 2018-10-5
 * @deprecated 该类已废弃，已使用继承 {@link ch.qos.logback.classic.helpers.MDCInsertingServletFilter} 方案替代替代
 */
@Deprecated
public class RidConvert extends ClassicConverter {

    @Override
    public String convert(final ILoggingEvent event) {
        return RequestId.get();
    }
}
