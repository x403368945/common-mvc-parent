package com.support.mvc.exception;


import com.utils.ICall;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 自定义异常: 封装 {@link RuntimeException} 用于处理 lambda 表达式处理异常
 *
 * @author 谢长春 2019-5-30
 */
public final class RTException extends RuntimeException {

    public static void call(final ICall call) {
        call.call();
    }

    public static RTException of(String message) {
        return new RTException(message);
    }

    public RTException(String msg) {
        super(msg);
    }

    /**
     * 在异常抛出前，可通过该方法收集异常消息
     *
     * @param consumer {@link Consumer}{@link Consumer<String:异常消息>}
     * @return {@link RTException}
     */
    public RTException go(final Consumer<String> consumer) {
        consumer.accept(this.getMessage());
        return this;
    }

    /**
     * 在异常抛出前，可通过该方法收集异常消息；
     * 判断当异常消息为空时，不执行该方法
     *
     * @param consumer {@link Consumer}{@link Consumer<String:异常消息>}
     * @return {@link RTException}
     */
    public RTException goNonNull(final Consumer<String> consumer) {
        if (Objects.nonNull(this.getMessage())) consumer.accept(this.getMessage());
        return this;
    }

}
