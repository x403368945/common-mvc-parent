package com.support.mvc.actions;

/**
 * 动作行为组件接口 : 向外公开模型实现的功能及数据结构
 *
 * @param <R> Result -> data 存储的元素
 * @author 谢长春 on 2017/10/12.
 */
public interface ISay<R> {
    /**
     * 向外公开模型实现的功能及数据结构
     *
     * @param userId Long 用户ID
     * @return Result<R>
     */
    R say(final Long userId);
}