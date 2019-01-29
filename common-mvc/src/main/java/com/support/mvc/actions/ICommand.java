package com.support.mvc.actions;


import com.support.mvc.entity.base.Param;

/**
 * 任务执行通用接口
 *
 * 
 * @author 谢长春 on 2018-10-14
 */
public interface ICommand {
    /**
     * 任务执行通用接口
     *
     * @param param   {@link Param} 参数
     * @param callback {@link ICallback} 消息回调
     * @param userId   {@link Long} 当前操作用户ID
     */
    void command(final Param param, final ICallback callback, final Long userId);
}
