package com.support.mvc.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * 数据访问接口方法定义，默认继承 QDSL 查询，建议查询尽量使用 QDSL 语法 <br>
 * 所有参数校验建议都写在 Service 中，数据逻辑不再校验参数
 *
 *
 * @param <E>
 * @author 谢长春 2017年7月14日 上午11:23:18
 */
@NoRepositoryBean
public interface IRepository<E, ID> extends ISearchRepository<E> {

    /**
     * 修改数据
     *
     * @param id     ID 数据ID
     * @param userId Long 操作用户ID
     * @param obj    E 实体对象
     * @return long 影响行数
     */
    @Modifying
    @Query
    default long update(final ID id, final Long userId, final E obj) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【update(final ID id, final Long userId, final E obj)】未实现"));
    }

    /**
     * 修改数据
     *
     * @param id     ID 数据ID
     * @param obj    E 实体对象
     * @return long 影响行数
     */
    @Modifying
    @Query
    default long update(final ID id, final E obj) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【update(final ID id, final E obj)】未实现"));
    }

    /**
     * 按ID删除，物理删除
     *
     * @param id     ID 数据ID
     * @param userId Long 操作用户ID
     * @return E 删除对象实体
     */
    @Modifying
    @Query
    default E deleteById(final ID id, final Long userId) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【deleteById(final ID id, final Long userId)】未实现"));
    }

    /**
     * 按ID和UUID删除，物理删除
     *
     * @param id     ID 数据ID
     * @param uid    String 数据UUID
     * @param userId Long 操作用户ID
     * @return E 删除对象实体
     */
    @Modifying
    @Query
    default E deleteByUid(final ID id, final String uid, final Long userId) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【deleteByUid(final ID id, final String uid, final Long userId)】未实现"));
    }

    /**
     * 按ID和UUID删除，物理删除
     *
     * @param id     ID 数据ID
     * @param uid    String 数据UUID
     * @return E 删除对象实体
     */
    @Modifying
    @Query
    default E deleteByUid(final ID id, final String uid) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【deleteByUid(final ID id, final String uid)】未实现"));
    }

    /**
     * 按ID删除，逻辑删除
     *
     * @param id     ID 数据ID
     * @param userId Long 操作用户ID
     * @return long 影响行数
     */
    @Modifying
    @Query
    default long markDeleteById(final ID id, final Long userId) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDeleteById(final ID id, final Long userId)】未实现"));
    }

    /**
     * 按ID删除，逻辑删除
     *
     * @param id     ID 数据ID
     * @return long 影响行数
     */
    @Modifying
    @Query
    default long markDeleteById(final ID id) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDeleteById(final ID id)】未实现"));
    }

    /**
     * 按ID和UUID删除，逻辑删除
     *
     * @param id     ID 数据ID
     * @param uid    String 数据UUID
     * @param userId Long 操作用户ID
     * @return long 影响行数
     */
    @Modifying
    @Query
    default long markDeleteByUid(final ID id, final String uid, final Long userId) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDeleteByUid(final ID id, final String uid, final Long userId)】未实现"));
    }

    /**
     * 按ID和UUID删除，逻辑删除
     *
     * @param id     ID 数据ID
     * @param uid    String 数据UUID
     * @return long 影响行数
     */
    @Modifying
    @Query
    default long markDeleteByUid(final ID id, final String uid) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDeleteByUid(final ID id, final String uid)】未实现"));
    }

    /**
     * 批量操作按ID删除，逻辑删除
     *
     * @param ids    List<ID> 数据ID集合
     * @param userId Long 操作用户ID
     * @return long 影响行数
     */
    @Modifying
    @Query
    default long markDeleteByIds(final List<ID> ids, final Long userId) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDeleteByIds(final List<ID> ids, final Long userId)】未实现"));
    }

    /**
     * 批量操作按ID删除，逻辑删除
     *
     * @param ids    List<ID> 数据ID集合
     * @return long 影响行数
     */
    @Modifying
    @Query
    default long markDeleteByIds(final List<ID> ids) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDeleteByIds(final List<ID> ids)】未实现"));
    }

    /**
     * 批量操作按ID和UUID删除，逻辑删除
     *
     * @param list   List<E> 数据ID和UUID构建的对象
     * @param userId Long 操作用户ID
     * @return long 影响行数
     */
    @Modifying
    @Query
    default long markDelete(final List<E> list, final Long userId) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDelete(final List<E> list, final Long userId)】未实现"));
    }

    /**
     * 批量操作按ID和UUID删除，逻辑删除
     *
     * @param list   List<E> 数据ID和UUID构建的对象
     * @return long 影响行数
     */
    @Modifying
    @Query
    default long markDelete(final List<E> list) {
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDelete(final List<E> list)】未实现"));
    }

}