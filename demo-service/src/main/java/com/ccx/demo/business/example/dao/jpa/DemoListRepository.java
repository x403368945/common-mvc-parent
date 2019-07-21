package com.ccx.demo.business.example.dao.jpa;

import com.ccx.demo.business.example.entity.QTabDemoList;
import com.ccx.demo.business.example.entity.TabDemoList;
import com.ccx.demo.enums.Radio;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.mvc.dao.IRepository;
import com.support.mvc.entity.base.Pager;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import static com.ccx.demo.config.init.BeanInitializer.Beans.jpaQueryFactory;

/**
 * 数据操作：
 *
 * @author 谢长春 on 2018-12-17
 */
public interface DemoListRepository extends
        JpaRepository<TabDemoList, Long>,
        IRepository<TabDemoList, Long> {
    QTabDemoList q = QTabDemoList.tabDemoList;

    @Modifying
    @Query
    default void updateNickName(final Long userId, final String nickname) {
        jpaQueryFactory.<JPAQueryFactory>get().update(q)
                .set(q.createUserName, nickname)
                .where(q.createUserId.eq(userId))
                .execute();
        jpaQueryFactory.<JPAQueryFactory>get().update(q)
                .set(q.modifyUserName, nickname)
                .where(q.modifyUserId.eq(userId))
                .execute();
    }

    @Override
    default long update(final Long id, final Long userId, final TabDemoList obj) {
        return obj.update(jpaQueryFactory.<JPAQueryFactory>get().update(q))
                .get()
                .where(q.id.eq(id).and(q.uid.eq(obj.getUid())).and(q.createUserId.eq(userId)).and(q.modifyTime.eq(obj.getModifyTime())))
                .execute();
    }

    @Override
    default TabDemoList deleteById(final Long id, final Long userId) {
        // 只能删除自己创建的数据
        return Optional
                .ofNullable(jpaQueryFactory.<JPAQueryFactory>get()
                        .selectFrom(q)
                        .where(q.id.eq(id).and(q.createUserId.eq(userId)))
                        .fetchOne()
                )
                .map(obj -> {
                    delete(obj);
                    return obj;
                })
                .orElseThrow(() -> new NullPointerException("数据物理删除失败：".concat(
                        TabDemoList.builder().id(id).createUserId(userId).build().json())
                ));
    }

    @Override
    default TabDemoList deleteByUid(final Long id, final String uid, final Long userId) {
        // 只能删除自己创建的数据，且使用 UUID 强校验；
        // userId 为可选校验，一般业务场景，能获取到 UUID 已经表示已经加强校验了
        return Optional
                .ofNullable(jpaQueryFactory.<JPAQueryFactory>get()
                        .selectFrom(q)
                        .where(q.id.eq(id).and(q.uid.eq(uid)).and(q.createUserId.eq(userId)))
                        .fetchOne()
                )
                .map(obj -> {
                    delete(obj);
                    return obj;
                })
                .orElseThrow(() -> new NullPointerException("数据物理删除失败：".concat(
                        TabDemoList.builder().id(id).uid(uid).createUserId(userId).build().json())
                ));
    }

    @Override
    default long markDeleteById(final Long id, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.modifyUserId, userId)
                .where(q.id.eq(id).and(q.createUserId.eq(userId)))
                .execute();
    }

    @Override
    default long markDeleteByUid(final Long id, final String uid, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.modifyUserId, userId)
                .where(q.id.eq(id).and(q.uid.eq(uid).and(q.createUserId.eq(userId))))
                .execute();
    }

    @Override
    default long markDeleteByIds(final List<Long> ids, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.modifyUserId, userId)
                .where(q.id.in(ids).and(q.createUserId.eq(userId)))
                .execute();
    }

    @Override
    default long markDelete(final List<TabDemoList> list, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.modifyUserId, userId)
                .where(q.id.in(list.stream().map(TabDemoList::getId).toArray(Long[]::new))
                        .and(q.createUserId.eq(userId))
                        .and(q.uid.in(list.stream().map(TabDemoList::getUid).toArray(String[]::new)))
                )
                .execute();
    }

    @Override
    default Optional<TabDemoList> findById(final Long id, final String uid) {
        return findOne(
                q.id.eq(id).and(q.uid.eq(uid))
                // 下面一行代码与上面效果是一样的，如果参数较多或本身就是一个对象，可以用 Example.of 构建简单查询对象，简单查询对象中的所有参数都使用 = 匹配，null 值忽略
                // Example.of(TabDemoList.builder().id(id).uid(uid).build())
        );
    }

    @Override
    default List<TabDemoList> findList(final TabDemoList condition) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }

    @Override
    default List<TabDemoList> findList(final TabDemoList condition, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(TabDemoList.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }

    @Override
    default QueryResults<TabDemoList> findPage(final TabDemoList condition, final Pager pager) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }


    @Override
    default QueryResults<TabDemoList> findPage(final TabDemoList condition, final Pager pager, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(TabDemoList.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }
}
