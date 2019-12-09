package com.ccx.demo.business.example.dao.jpa;

import com.ccx.demo.business.example.entity.TabConvert;
import com.ccx.demo.business.example.entity.QTabConvert;
import com.ccx.demo.enums.Radio;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.mvc.dao.IRepository;
import com.support.mvc.entity.base.Pager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.ccx.demo.config.init.BeanInitializer.Beans.jpaQueryFactory;

/**
 * 数据操作：测试自定义 Convert 表
 *
 * @author 谢长春 on 2019-08-21
 */
public interface ConvertRepository extends
        JpaRepository<TabConvert, Long>,
        IRepository<TabConvert, Long> {
    QTabConvert q = QTabConvert.tabConvert;

    @Override
    default long update(final Long id, final Long userId, final TabConvert obj) {
        return obj.update(jpaQueryFactory.<JPAQueryFactory>get().update(q))
                .get()
                .where(q.id.eq(id).and(q.uid.eq(obj.getUid())).and(q.insertUserId.eq(userId)).and(q.updateTime.eq(obj.getUpdateTime())))
                .execute();
    }

    @Override
    default TabConvert deleteById(final Long id, final Long userId) {
        // 只能删除自己创建的数据
        return Optional
                .ofNullable(jpaQueryFactory.<JPAQueryFactory>get()
                        .selectFrom(q)
                        .where(q.id.eq(id).and(q.insertUserId.eq(userId)))
                        .fetchOne()
                )
                .map(obj -> {
                    delete(obj);
                    return obj;
                })
                .orElseThrow(() -> new NullPointerException("数据物理删除失败：".concat(
                        TabConvert.builder().id(id).insertUserId(userId).build().json())
                ));
    }

    @Override
    default TabConvert deleteByUid(final Long id, final String uid, final Long userId) {
        // 只能删除自己创建的数据，且使用 UUID 强校验；
        // userId 为可选校验，一般业务场景，能获取到 UUID 已经表示已经加强校验了
        return Optional
                .ofNullable(jpaQueryFactory.<JPAQueryFactory>get()
                        .selectFrom(q)
                        .where(q.id.eq(id).and(q.uid.eq(uid)).and(q.insertUserId.eq(userId)))
                        .fetchOne()
                )
                .map(obj -> {
                    delete(obj);
                    return obj;
                })
                .orElseThrow(() -> new NullPointerException("数据物理删除失败：".concat(
                        TabConvert.builder().id(id).uid(uid).insertUserId(userId).build().json())
                ));
    }

    @Override
    default long markDeleteById(final Long id, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id).and(q.insertUserId.eq(userId)).and(q.deleted.eq(Radio.NO)))
                .execute();
    }

    @Override
    default long markDeleteByUid(final Long id, final String uid, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id).and(q.uid.eq(uid).and(q.insertUserId.eq(userId))).and(q.deleted.eq(Radio.NO)))
                .execute();
    }

    @Override
    default long markDeleteByIds(final List<Long> ids, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.in(ids).and(q.insertUserId.eq(userId)).and(q.deleted.eq(Radio.NO)))
                .execute();
    }

    @Override
    default long markDelete(final List<TabConvert> list, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.in(list.stream().map(TabConvert::getId).toArray(Long[]::new))
                        .and(q.insertUserId.eq(userId)).and(q.deleted.eq(Radio.NO))
                        .and(q.uid.in(list.stream().map(TabConvert::getUid).toArray(String[]::new)))
                )
                .execute();
    }

    @Override
    default List<TabConvert> findList(final TabConvert condition) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }

    @Override
    default List<TabConvert> findList(final TabConvert condition, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(TabConvert.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }

    @Override
    default QueryResults<TabConvert> findPage(final TabConvert condition, final Pager pager) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }


    @Override
    default QueryResults<TabConvert> findPage(final TabConvert condition, final Pager pager, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(TabConvert.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }
}
