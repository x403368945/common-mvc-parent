package com.support.mvc.service.str;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.support.mvc.entity.base.Pager;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

/**
 * 服务接口基础方法定义
 *
 * @param <E>
 * @author 谢长春 2017年7月14日 上午11:23:18
 */
@Validated
interface ISearch<E> {

    /**
     * 按ID查询对象
     *
     * @param id {@link String} 数据ID
     * @return {@link Optional<E>} 实体对象
     */
    default Optional<E> findById(@NotBlank(message = "【id】不能为空") final String id) {
//		return repository.findById(id);
        throw new NullPointerException(this.getClass().getName().concat("：方法【findById(final String id)】未实现"));
    }

    /**
     * 按条件查询列表
     *
     * @param condition E 查询条件
     * @return {@link List<E>} 结果集合
     */
    default @NotNull(message = "返回值不能为null") List<E> findList(
            @NotNull(message = "【condition】不能为null") final E condition) {
//		return repository.findList(condition, sorts);
        throw new NullPointerException(this.getClass().getName().concat("：方法【findList(final E condition)】未实现"));
    }

    /**
     * 按条件查询列表
     *
     * @param condition E 查询条件
     * @param exps      {@link Expression} 指定查询字段
     * @return {@link List<E>} 结果集合
     */
    default @NotNull(message = "findList...exps:返回值不能为null") List<E> findList(
            @NotNull(message = "findList...exps.arg0:【condition】不能为null") final E condition, final Expression<?>... exps) {
//		return repository.findList(condition, sorts, exps);
        throw new NullPointerException(this.getClass().getName().concat("：方法【findList(final E condition, final Expression<?>... exps)】未实现"));
    }

    /**
     * 按条件分页查询列表
     *
     * @param condition E 查询条件
     * @param pager     {@link Pager} 分页排序集合
     * @return {@link QueryResults<E>} 分页对象
     */
    default @NotNull(message = "返回值不能为null") QueryResults<E> findPage(
            @NotNull(message = "【condition】不能为null") final E condition, final Pager pager) {
//		return repository.findPage(condition, pager);
        throw new NullPointerException(this.getClass().getName().concat("：方法【findPage(final E condition, final Pager pager)】未实现"));
    }

    /**
     * 按条件分页查询列表
     *
     * @param condition E 查询条件
     * @param pager     {@link Pager}  分页排序集合
     * @param exps      {@link Expression} 指定查询字段
     * @return {@link QueryResults<E>} 分页对象
     */
    default @NotNull(message = "findPage...exps:返回值不能为null") QueryResults<E> findPage(
            @NotNull(message = "findPage...exps.arg0:【condition】不能为null") final E condition, final Pager pager, final Expression<?>... exps) {
//		return repository.findPage(condition, pager, exps);
        throw new NullPointerException(this.getClass().getName().concat("：方法【findPage(final E condition, final Pager pager, final Expression<?>... exps)】未实现"));
    }
}