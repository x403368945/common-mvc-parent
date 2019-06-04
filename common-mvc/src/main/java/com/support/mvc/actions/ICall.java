package com.support.mvc.actions;

import lombok.SneakyThrows;

/**
 * 执行:同 {@link IExecute} 一样
 *
 * @author 谢长春 2018-10-11
 */
public interface ICall {
    /**
     * 执行代码
     */
    @SneakyThrows
    void call();
}