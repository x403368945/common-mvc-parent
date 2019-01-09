package com.mvc.dao;

import com.mvc.entity.base.Pager;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * 数据访问接口方法定义，默认继承 QDSL 查询，建议查询尽量使用 QDSL 语法
 * 所有参数校验建议都写在 Service 中，数据逻辑不再校验参数
 *
 *
 * @param <E>
 * @author 谢长春 2017年7月14日 上午11:23:18
 */
@NoRepositoryBean
public interface ISearchRepository<E> extends QuerydslPredicateExecutor<E> {

    /**
     * 按条件查询列表
     *
     * @param condition 查询条件
     * @return List<E> 结果集合
     */
    default List<E> findList(final E condition) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【findList(final E condition)】未实现"));
    }

    /**
     * 按条件查询列表
     *
     * @param condition 查询条件
     * @param exps      {@link Expression} 指定查询字段
     * @return List<E> 结果集合
     */
    default List<E> findList(final E condition, final Expression<?> ...exps) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【findList(final E condition, final Expression<?>... exps)】未实现"));
    }

    /**
     * 按条件分页查询列表
     *
     * @param condition 查询条件
     * @param pager     Pager 分页排序集合
     * @return QueryResults<E> 分页对象
     */
    default QueryResults<E> findPage(final E condition, final Pager pager) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【findPage(final E condition, final Pager pager)】未实现"));
    }

    /**
     * 按条件分页查询列表
     *
     * @param condition 查询条件
     * @param pager     {@link Pager} 分页排序集合
     * @param exps      {@link Expression} 指定查询字段
     * @return QueryResults<E> 分页对象
     */
    default QueryResults<E> findPage(final E condition, final Pager pager, final Expression<?> ...exps) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【findPage(final E condition, final Pager pager, final Expression<?>... exps)】未实现"));
    }
}