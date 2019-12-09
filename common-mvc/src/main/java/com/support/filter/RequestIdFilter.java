package com.support.filter;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import com.google.common.base.Strings;
import com.utils.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * <pre>
 * 将请求标记存入 ThreadLocal
 * 警告：多线程时需要特殊处理
 * final Map<String, String> mdc = MDC.getCopyOfContextMap(); // 复制主线程 ThreadLocal
 * new Thread(() -> {
 *     try {
 *         MDC.setContextMap(mdc); // 设置子线程 ThreadLocal
 *         // 子线程代码
 *     } finally {
 *         MDC.clear(); // 清除子线程 ThreadLocal
 *     }
 * }).start();
 *
 * @author 谢长春 2019/6/6
 */
@Slf4j
//@WebFilter(urlPatterns = "/*", filterName="RequestIdFilter")
public class RequestIdFilter extends MDCInsertingServletFilter {

    /**
     * 获取请求标记生成适配器
     *
     * @return {@link IRidAdapter}
     */
    protected IRidAdapter getRidAdapter() {
        return new DefaultAdapter();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String rid = ((HttpServletRequest) request).getHeader("rid");
            if (Strings.isNullOrEmpty(rid)) {
                rid = Optional.ofNullable(getRidAdapter()).orElseGet(DefaultAdapter::new).getRid();
            }
            MDC.put("rid", rid);
            super.doFilter(request, response, chain);
            ((HttpServletResponse) response).addHeader("rid", RequestIdFilter.get());
        } finally {
            MDC.remove("rid");
        }
    }

    /**
     * 获取 ThreadLocal 值
     *
     * @return {@link String}
     */
    public static String get() {
        return MDC.get("rid");
    }

    /**
     * 请求标记适配接口
     */
    public interface IRidAdapter {
        /**
         * 获取请求标记
         *
         * @return {@link String}
         */
        String getRid();
    }

    /**
     * 使用 6 位随机数 + 2 位数字或大小写字母
     */
    public static class DefaultAdapter implements IRidAdapter {
        public String getRid() {
            return Util.random(6).concat(RandomStringUtils.randomAlphanumeric(2));
        }
    }

    /**
     * 使用 uuid
     */
    public static class UuidAdapter implements IRidAdapter {
        public String getRid() {
            return Util.uuid32();
        }
    }

    /**
     * 使用 6 位随机数
     */
    public static class RandomNumberAdapter implements IRidAdapter {
        public String getRid() {
            return Util.random(6);
        }
    }
}