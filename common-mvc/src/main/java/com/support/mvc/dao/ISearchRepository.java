package com.support.mvc.dao;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.support.mvc.entity.base.Pager;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * 数据访问接口方法定义，默认继承 QDSL 查询，建议查询尽量使用 QDSL 语法
 * 所有参数校验建议都写在 Service 中，数据逻辑不再校验参数
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
    default QueryResults<E> findPage(final E condition, final Pager pager, final Expression<?>... exps) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【findPage(final E condition, final Pager pager, final Expression<?>... exps)】未实现"));
    }

    /**
     * 按条件查询列表
     *
     * @param condition 查询条件
     * @param exps      {@link Expression} 指定查询字段
     * @return List<E> 结果集合
     */
    default List<E> findList(final E condition, final Expression<?>... exps) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【findList(final E condition, final Expression<?>... exps)】未实现"));
    }

    /**
     * 按条件查询列表，将查询结果投影到指定 VO 类后返回，VO 类必须继承实体
     *
     * @param condition 查询条件:可使用继承实体的 VO 类传参,扩展实体默认的查询
     * @param clazz     继承实体的 VO 类
     * @return List<E> 结果集合
     */
    default <T extends E> List<T> findListProjection(final E condition, final Class<T> clazz) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【findList(final E condition, final Class<T> clazz)】未实现"));
    }

    /**
     * 按条件查询列表，将查询结果投影到指定 VO 类后返回，VO 类必须继承实体
     *
     * @param condition 查询条件:可使用继承实体的 VO 类传参,扩展实体默认的查询
     * @param clazz     继承实体的 VO 类
     * @param exps      {@link Expression} 指定查询字段
     * @return List<E> 结果集合
     */
    default <T extends E> List<T> findListProjection(final E condition, final Class<T> clazz, final Expression<?>... exps) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【findList(final E condition, final Class<T> clazz, final Expression<?>... exps)】未实现"));
    }

    /**
     * 按条件分页查询列表，将查询结果投影到指定 VO 类后返回，VO 类必须继承实体
     *
     * @param condition 查询条件:可使用继承实体的 VO 类传参,扩展实体默认的查询
     * @param pager     Pager 分页排序集合
     * @param clazz     继承实体的 VO 类
     * @return QueryResults<E> 分页对象
     */
    default <T extends E> QueryResults<T> findPageProjection(final E condition, final Pager pager, final Class<T> clazz) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【findPageProjection(final E condition, final Pager pager, final Class<T> clazz)】未实现"));
    }

    /**
     * 按条件分页查询列表，将查询结果投影到指定 VO 类后返回，VO 类必须继承实体
     *
     * @param condition 查询条件:可使用继承实体的 VO 类传参,扩展实体默认的查询
     * @param pager     {@link Pager} 分页排序集合
     * @param clazz     继承实体的 VO 类
     * @param exps      {@link Expression} 指定查询字段
     * @return QueryResults<E> 分页对象
     */
    default <T extends E> QueryResults<T> findPageProjection(final E condition, final Pager pager, final Class<T> clazz, final Expression<?>... exps) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【findPageProjection(final E condition, final Pager pager, final Class<T> clazz, final Expression<?>... exps)】未实现"));
    }
}