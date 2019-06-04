package com.support.mvc.exception;

import com.support.mvc.actions.ICall;

import java.util.function.Consumer;

/**
 * 自定义异常: 封装 {@link RuntimeException} 用于处理 lambda 表达式处理异常
 *
 * @author 谢长春 2019-5-30
 */
public class RTException extends RuntimeException {

    public static void call(final ICall call) {
        call.call();
    }

    public static RTException of(String message) {
        return new RTException(message);
    }

    public RTException(String msg) {
        super(msg);
    }

    public RTException go(final Consumer<String> consumer) {
        consumer.accept(this.getMessage());
        return this;
    }
}
