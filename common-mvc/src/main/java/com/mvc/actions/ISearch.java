package com.mvc.actions;

/**
 * 动作行为组件接口 : 查询或加载数据
 * @param <R> Result -> data 存储的元素
 * @param <P> 入参数据对象
 * @author 谢长春 on 2017/10/12.
 */
public interface ISearch<R, P> {
    /**
     * 查询或加载数据（查询模型数据）
     * @param param P 参数
     * @param userId Long 用户ID
     * @return Result<R>
     */
    IResult<R> search(P param, final Long userId);
}