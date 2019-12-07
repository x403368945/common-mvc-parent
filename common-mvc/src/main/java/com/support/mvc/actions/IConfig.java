package com.support.mvc.actions;

/**
 * 动作行为组件接口 : 配置（配置模型）
 * @param <R> Result -> data 存储的元素
 * @param <P> 入参数据对象
 *
 * @author 谢长春 on 2017/10/12.
 */
public interface IConfig<R, P> {
    /**
     * 配置（配置模型，关联数据源及字段映射）
     * @param param P 参数
     * @param userId Long 用户ID
     * @return Result<R>
     */
    R config(P param, final Long userId);
}