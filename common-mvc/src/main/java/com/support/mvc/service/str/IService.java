package com.support.mvc.service.str;

import com.support.mvc.entity.validated.IMarkDelete;
import com.support.mvc.entity.validated.ISave;
import com.support.mvc.entity.validated.IUpdate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.validation.groups.Default;
import java.util.List;

/**
 * 服务接口基础方法规范定义
 *
 *
 * @param <E>
 * @author 谢长春 2017年7月14日 上午11:23:18
 */
@Validated
public interface IService<E> extends ISearch<E> {
//	IRepository<E> getFindRepository();

    /**
     * 保存；使用 aop 拦截，通过 @ServiceAspect 注解设置 uid 和 用户信息
     *
     * @param obj    E 实体对象
     * @param userId {@link Long} 操作用户ID
     * @return E 实体对象
     */
    @Validated({Default.class, ISave.class})
    @Transactional(rollbackFor = Exception.class)
    default @NotNull(message = "返回值不能为null") E save(
            @Valid @NotNull(message = "【obj】不能为null") final E obj,
            @NotNull(message = "【userId】不能为null") @Positive(message = "【userId】必须大于0") final Long userId) {
/* 以下字段将会在 @ServiceAspect 中设置
        obj.setId(null);
        obj.setUid(Util.uuid());
        obj.setCreateUserId(userId);
        obj.setModifyUserId(userId);
*/
//        return repository.save(obj);
        throw new NullPointerException(this.getClass().getName().concat("：方法【save(final E obj, final Long userId)】未实现"));
    }

    /**
     * 批量保存；使用 aop 拦截，通过 @ServiceAspect 注解设置 uid 和 用户信息
     *
     * @param list   {@link List<E>}  实体对象集合
     * @param userId {@link Long} 操作用户ID
     * @return List<E> 实体对象集合
     */
    @Validated({Default.class, ISave.class})
    @Transactional(rollbackFor = Exception.class)
    default @NotNull(message = "返回值不能为null") List<E> saveAll(
            @NotEmpty(message = "【list】不能为空") final List<@Valid @NotNull E> list,
            @NotNull(message = "【userId】不能为null") @Positive(message = "【userId】必须大于0") final Long userId) {
/* 以下字段将会在 @ServiceAspect 中设置
        obj.setId(null);
        obj.setUid(Util.uuid());
        obj.setCreateUserId(userId);
        obj.setModifyUserId(userId);
*/
//        return repository.saveAll(list);
        throw new NullPointerException(this.getClass().getName().concat("：方法【saveAll(final List<E> list, final Long userId)】未实现"));
    }

    /**
     * 更新数据；使用 aop 拦截，通过 @ServiceAspect 注解设置用户信息
     *
     * @param id     {@link String} 数据ID
     * @param userId {@link Long} 操作用户ID
     * @param obj    E 实体对象
     */
    @Validated({Default.class, IUpdate.class})
    @Transactional(rollbackFor = Exception.class)
    default void update(@NotBlank(message = "【id】不能为空") final String id,
                        @NotNull(message = "【userId】不能为null") @Positive(message = "【userId】必须大于0") final Long userId,
                        @Valid @NotNull(message = "【obj】不能为null") final E obj) {
/* 以下字段将会在 @ServiceAspect 中设置
        obj.setId(id);
        obj.setModifyUserId(userId);
*/
//        UpdateRowsException.asserts(repository.update(id, userId, obj));
        throw new NullPointerException(this.getClass().getName().concat("：方法【update(final String id, final Long userId, final E obj)】未实现"));
    }

    /**
     * 按ID删除，物理删除；执行物理删除前先查询到数据，等待删除成功之后返回该数据对象，通过 AOP 拦截记录到删除日志中
     *
     * @param id     {@link String} 数据ID
     * @param userId {@link Long} 操作用户ID
     * @return E 删除对象数据实体
     */
    @Transactional(rollbackFor = Exception.class)
    default E deleteById(@NotBlank(message = "【id】不能为空") final String id,
                         @NotNull(message = "【userId】不能为null") @Positive(message = "【userId】必须大于0") final Long userId) {
//        return repository.deleteById(id, userId);
        throw new NullPointerException(this.getClass().getName().concat("：方法【deleteById(final String id, final Long userId)】未实现"));
    }

    /**
     * 按ID和UUID删除，物理删除；执行物理删除前先查询到数据，等待删除成功之后返回该数据对象，通过 AOP 拦截记录到删除日志中
     *
     * @param id     {@link String} 数据ID
     * @param uid    {@link String} 数据UUID
     * @param userId {@link Long} 操作用户ID
     * @return E 删除对象数据实体
     */
    @Transactional(rollbackFor = Exception.class)
    default E deleteByUid(@NotBlank(message = "【id】不能为空") final String id,
                          @NotNull(message = "【uid】不能为null") @Size(min = 32, max = 32, message = "【uid】长度必须是32位") final String uid,
                          @NotNull(message = "【userId】不能为null") @Positive(message = "【userId】必须大于0") final Long userId) {
//        return repository.deleteById(id, uid, userId);
        throw new NullPointerException(this.getClass().getName().concat("：方法【deleteByUid(final String id, final String uid, final Long userId)】未实现"));
    }

    /**
     * 按ID删除，逻辑删除
     *
     * @param id     {@link String} 数据ID
     * @param userId {@link Long} 操作用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    default void markDeleteById(@NotBlank(message = "【id】不能为null") final String id,
                                @NotNull(message = "【userId】不能为null") @Positive(message = "【userId】必须大于0") final Long userId) {
//        DeleteRowsException.asserts(repository.markDeleteById(id, userId));
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDeleteById(final String id, final Long userId)】未实现"));
    }

    /**
     * 按ID和UUID删除，逻辑删除
     *
     * @param id     {@link String} 数据ID
     * @param uid    {@link String} 数据UUID
     * @param userId {@link Long} 操作用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    default void markDeleteByUid(@NotBlank(message = "【id】不能为null") final String id,
                                 @NotNull(message = "【uid】不能为null") @Size(min = 32, max = 32, message = "【uid】长度必须是32位") final String uid,
                                 @NotNull(message = "【userId】不能为null") @Positive(message = "【userId】必须大于0") final Long userId) {
//        DeleteRowsException.asserts(repository.markDeleteById(id, uid, userId));
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDeleteByUid(final String id, final String uid, final Long userId)】未实现"));
    }

    /**
     * 批量操作按ID删除，逻辑删除
     *
     * @param ids    {@link List<String>} 数据ID
     * @param userId {@link Long} 操作用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    default void markDeleteByIds(@NotEmpty(message = "【ids】不能为空") final List<@NotBlank String> ids,
                                 @NotNull(message = "【userId】不能为null") @Positive(message = "【userId】必须大于0") final Long userId) {
//        DeleteRowsException.batch(repository.markDeleteByIds(ids, userId));
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDeleteByIds(final List<String> ids, final Long userId)】未实现"));
    }

    /**
     * 批量操作按ID和UUID删除，逻辑删除
     *
     * @param list   {@link List<E>} 数据ID
     * @param userId {@link Long} 操作用户ID
     */
    @Validated({Default.class, IMarkDelete.class})
    @Transactional(rollbackFor = Exception.class)
    default void markDelete(@NotEmpty(message = "【list】不能为空") final List<@Valid @NotNull E> list,
                            @NotNull(message = "【userId】不能为null") @Positive(message = "【userId】必须大于0") final Long userId) {
//        DeleteRowsException.batch(repository.markDelete(list, userId));
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDelete(final List<E> list, final Long userId)】未实现"));
    }
}