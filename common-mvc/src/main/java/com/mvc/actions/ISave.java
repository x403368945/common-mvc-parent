package com.mvc.actions;

/**
 * 动作行为组件接口 : 保存（保存模型数据）
 * @param <R> Result -> data 存储的元素
 * @param <P> 入参数据对象
 * @author 谢长春 on 2017/10/12.
 */
public interface ISave<R, P> {
    /**
     * 保存（保存数据数据）
     * @param param P 参数
     * @param userId Long 用户ID
     * @return Result<R>
     */
    IResult<R> save(P param, final Long userId);
}