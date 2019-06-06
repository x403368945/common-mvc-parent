package com.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 将当前请求唯一标记写入日志
 *
 * @author 谢长春 2018-10-5
 */
public class UidConvert extends ClassicConverter {

    @Override
    public String convert(final ILoggingEvent event) {
        return Reqid.get();
    }
}
