package com.support.mvc.service.str;

import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.validated.IMarkDelete;
import com.support.mvc.entity.validated.ISave;
import com.support.mvc.entity.validated.IUpdate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Set;

/**
 * 服务接口基础方法规范定义
 *
 * @param <E>
 * @author 谢长春 2017年7月14日 上午11:23:18
 */
public interface IOpenService<E> extends IService<E> {
//	IRepository<E> getFindRepository();

    /**
     * 保存；使用 aop 拦截，通过 @ServiceAspect 注解设置
     *
     * @param obj E 实体对象
     * @return E 实体对象
     */
    @Validated({Default.class, ISave.class})
    @Transactional(rollbackFor = Exception.class)
    default @NotNull(message = "返回值不能为null") E save(
            @Valid @NotNull(message = "【obj】不能为null") final E obj) {
/* 以下字段将会在 @ServiceAspect 中设置
        obj.setId(null);
*/
//        return repository.save(obj);
        throw new NullPointerException(this.getClass().getName().concat("：方法【save(final E obj)】未实现"));
    }

    /**
     * 批量保存；使用 aop 拦截，通过 @ServiceAspect 注解设置 id
     *
     * @param list {@link List<E>}  实体对象集合
     * @return List<E> 实体对象集合
     */
    @Validated({Default.class, ISave.class})
    @Transactional(rollbackFor = Exception.class)
    default @NotNull(message = "返回值不能为null") List<E> saveAll(
            @NotEmpty(message = "【list】不能为空") final List<@Valid @NotNull E> list) {
/* 以下字段将会在 @ServiceAspect 中设置
        obj.setId(null);
*/
//        return repository.saveAll(list);
        throw new NullPointerException(this.getClass().getName().concat("：方法【saveAll(final List<E> list)】未实现"));
    }

    /**
     * 更新数据；使用 aop 拦截，通过 @ServiceAspect 注解设置用户信息
     *
     * @param id  {@link String} 数据ID
     * @param obj E 实体对象
     */
    @Validated({Default.class, IUpdate.class})
    @Transactional(rollbackFor = Exception.class)
    default void update(@NotBlank(message = "【id】不能为空") final String id,
                        @Valid @NotNull(message = "【obj】不能为null") final E obj) {
/* 以下字段将会在 @ServiceAspect 中设置
        obj.setId(id);
        obj.setUpdateUserId(userId);
*/
//        UpdateRowsException.asserts(repository.update(id, userId, obj));
        throw new NullPointerException(this.getClass().getName().concat("：方法【update(final String id, final E obj)】未实现"));
    }

    /**
     * 按ID删除，物理删除；执行物理删除前先查询到数据，等待删除成功之后返回该数据对象，通过 AOP 拦截记录到删除日志中
     *
     * @param id {@link String} 数据ID
     * @return E 删除对象数据实体
     */
    @Transactional(rollbackFor = Exception.class)
    default E deleteById(@NotBlank(message = "【id】不能为空") final String id) {
//        return repository.deleteById(id, userId);
        throw new NullPointerException(this.getClass().getName().concat("：方法【deleteById(final String id)】未实现"));
    }

    /**
     * 按ID删除，逻辑删除
     *
     * @param id {@link String} 数据ID
     */
    @Transactional(rollbackFor = Exception.class)
    default void markDeleteById(@NotBlank(message = "【id】不能为空") final String id) {
//        DeleteRowsException.asserts(repository.markDeleteById(id, userId));
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDeleteById(final String id)】未实现"));
    }

    /**
     * 批量操作按ID删除，逻辑删除
     *
     * @param ids {@link List<String>} 数据ID
     */
    @Transactional(rollbackFor = Exception.class)
    default void markDeleteByIds(@NotEmpty(message = "【ids】不能为空") final Set<@NotBlank String> ids) {
//        DeleteRowsException.batch(repository.markDeleteByIds(ids, userId));
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDeleteByIds(final Set<String> ids)】未实现"));
    }

    /**
     * 批量操作按ID删除，逻辑删除
     *
     * @param list {@link List<MarkDelete>} 数据ID
     */
    @Validated({Default.class, IMarkDelete.class})
    @Transactional(rollbackFor = Exception.class)
    default void markDelete(@NotEmpty(message = "【list】不能为null") final List<@Valid @NotNull MarkDelete> list) {
//        DeleteRowsException.batch(repository.markDelete(list, userId));
        throw new NullPointerException(this.getClass().getName().concat("：方法【markDelete(final List<MarkDelete> list)】未实现"));
    }
}