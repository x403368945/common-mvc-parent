package com.support.mvc.actions;

/**
 * 动作行为组件接口 : 下载（下载模型模板）
 * @param <R> Result -> data 存储的元素
 * @param <P> 入参数据对象
 *
 * @author 谢长春 on 2017/10/12.
 */
public interface IDownload<R, P> {
    /**
     * 下载（下载模型模板）
     * @param param P 参数
     * @param userId Long 用户ID
     * @return Result<R>
     */
    IResult<R> download(P param, final Long userId);
}