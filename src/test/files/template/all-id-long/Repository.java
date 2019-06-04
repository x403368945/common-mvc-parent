package com.ccx.business.{javaname}.dao.jpa;

import com.ccx.business.{javaname}.entity.{TabName};
import com.ccx.business.{javaname}.entity.Q{TabName};
import {pkg}.enums.Radio;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.mvc.dao.IRepository;
import com.support.mvc.entity.base.Pager;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static {pkg}.config.init.BeanInitializer.Beans.jpaQueryFactory;

/**
 * 数据操作：{comment}
 *
 * @author 谢长春 on {date}
 */
public interface {JavaName}Repository extends
        JpaRepository<{TabName}, {ID}>,
        IRepository<{TabName}, {ID}> {
    Q{TabName} q = Q{TabName}.{tabName};

    @Override
    default long update(final {ID} id, final Long userId, final {TabName} obj) {
        return obj.update(jpaQueryFactory.<JPAQueryFactory>get().update(q))
                .get()
                .where(q.id.eq(id).and(q.createUserId.eq(userId)).and(q.modifyTime.eq(obj.getModifyTime())))
                .execute();
    }

    @Override
    default {TabName} deleteById(final {ID} id, final Long userId) {
        // 只能删除自己创建的数据
        return findOne(Example.of({TabName}.builder().id(id).createUserId(userId).build()))
                .map(obj -> {
                    delete(obj);
                    return obj;
                })
                .orElseThrow(() -> new NullPointerException("数据物理删除失败：".concat(
                        {TabName}.builder().id(id).createUserId(userId).build().json())
                ));
    }

//    @Override
//    default {TabName} deleteByUid(final {ID} id, final String uid, final Long userId) {
//        // 只能删除自己创建的数据，且使用 UUID 强校验；
//        // userId 为可选校验，一般业务场景，能获取到 UUID 已经表示已经加强校验了
//        return findOne(Example.of({TabName}.builder().id(id).uid(uid).createUserId(userId).build()))
//                .map(obj -> {
//                    delete(obj);
//                    return obj;
//                })
//                .orElseThrow(() -> new NullPointerException("数据物理删除失败：".concat(
//                        {TabName}.builder().id(id).uid(uid).createUserId(userId).build().json())
//                ));
//    }

    @Override
    default long markDeleteById(final {ID} id, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.modifyUserId, userId)
                .where(q.id.eq(id).and(q.createUserId.eq(userId)))
                .execute();
    }

//    @Override
//    default long markDeleteByUid(final {ID} id, final String uid, final Long userId) {
//        return jpaQueryFactory.<JPAQueryFactory>get()
//                .update(q)
//                .set(q.deleted, Radio.YES)
//                .set(q.modifyUserId, userId)
//                .where(q.id.eq(id).and(q.uid.eq(uid).and(q.createUserId.eq(userId))))
//                .execute();
//    }

    @Override
    default long markDeleteByIds(final List<{ID}> ids, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.modifyUserId, userId)
                .where(q.id.in(ids).and(q.createUserId.eq(userId)))
                .execute();
    }

    @Override
    default long markDelete(final List<{TabName}> list, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.modifyUserId, userId)
                .where(q.id.in(list.stream().map({TabName}::getId).toArray(Long[]::new))
                        .and(q.createUserId.eq(userId))
//                        .and(q.uid.in(list.stream().map({TabName}::getUid).toArray(String[]::new)))
                )
                .execute();
    }

    @Override
    default Optional<{TabName}> findById(final {ID} id, final String uid) {
        return findOne(
                q.id.eq(id)//.and(q.uid.eq(uid))
                // 下面一行代码与上面效果是一样的，如果参数较多或本身就是一个对象，可以用 Example.of 构建简单查询对象，简单查询对象中的所有参数都使用 = 匹配，null 值忽略
                // Example.of({TabName}.builder().id(id).uid(uid).build())
        );
    }

    @Override
    default List<{TabName}> findList(final {TabName} condition) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }

    @Override
    default List<{TabName}> findList(final {TabName} condition, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean({TabName}.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }

    @Override
    default QueryResults<{TabName}> findPage(final {TabName} condition, final Pager pager) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }


    @Override
    default QueryResults<{TabName}> findPage(final {TabName} condition, final Pager pager, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean({TabName}.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }
}
