package com.support.mvc.actions;

/**
 * 动作行为组件接口 : 校验数据（校验上传的数据源）
 *
 * @param <R> Result -> data 存储的元素
 * @param <P> 入参数据对象
 * @author 谢长春 on 2017/10/12.
 */
public interface ICheck<R, P> {
    /**
     * 校验数据（校验上传的数据源并设置默认关联项）
     *
     * @param param  P 参数
     * @param userId Long 用户ID
     * @return Result<R>
     */
    R check(P param, final Long userId);
}