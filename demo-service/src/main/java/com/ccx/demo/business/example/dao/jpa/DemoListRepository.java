package com.ccx.demo.business.example.dao.jpa;

import com.alibaba.fastjson.JSON;
import com.ccx.demo.business.example.entity.QTabDemoList;
import com.ccx.demo.business.example.entity.TabDemoList;
import com.ccx.demo.business.example.vo.QTabDemoListVO;
import com.ccx.demo.business.example.vo.TabDemoListVO;
import com.ccx.demo.enums.Radio;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.mvc.dao.IRepository;
import com.support.mvc.entity.base.Pager;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    default long update(final Long id, final Long userId, final TabDemoList obj) {
        return obj.update(jpaQueryFactory.<JPAQueryFactory>get().update(q))
                .get()
                .where(q.id.eq(id).and(q.uid.eq(obj.getUid())).and(q.insertUserId.eq(userId)).and(q.updateTime.eq(obj.getUpdateTime())))
                .execute();
    }

    @Override
    default TabDemoList deleteById(final Long id, final Long userId) {
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
                        TabDemoList.builder().id(id).insertUserId(userId).build().json())
                ));
    }

    @Override
    default TabDemoList deleteByUid(final Long id, final String uid, final Long userId) {
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
                        TabDemoList.builder().id(id).uid(uid).insertUserId(userId).build().json())
                ));
    }

    @Override
    default long markDeleteById(final Long id, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id).and(q.insertUserId.eq(userId)))
                .execute();
    }

    @Override
    default long markDeleteByUid(final Long id, final String uid, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id).and(q.uid.eq(uid).and(q.insertUserId.eq(userId))))
                .execute();
    }

    @Override
    default long markDeleteByIds(final List<Long> ids, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.in(ids).and(q.insertUserId.eq(userId)))
                .execute();
    }

    @Override
    default long markDelete(final List<TabDemoList> list, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.in(list.stream().map(TabDemoList::getId).toArray(Long[]::new))
                        .and(q.insertUserId.eq(userId))
                        .and(q.uid.in(list.stream().map(TabDemoList::getUid).toArray(String[]::new)))
                )
                .execute();
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

    default <T extends TabDemoList> List<T> findListProjection(final TabDemoList condition, final Class<T> clazz) {
        List<TabDemoListVO> fetch = jpaQueryFactory.<JPAQueryFactory>get()
                .select(q.as(QTabDemoListVO.class))
                .from(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
        return null;
    }

    @Override
    default <T extends TabDemoList> List<T> findListProjection(final TabDemoList condition, final Class<T> clazz, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(clazz, exps))
                .from(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }

    @Override
    default <T extends TabDemoList> QueryResults<T> findPageProjection(final TabDemoList condition, final Pager pager, final Class<T> clazz) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(clazz, q))
                .from(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }

    @Override
    default <T extends TabDemoList> QueryResults<T> findPageProjection(final TabDemoList condition, final Pager pager, final Class<T> clazz, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(clazz, exps))
                .from(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }


    /**
     * 自定方言
     */
    default void findListTest() {
        System.out.println("group_concat:" + JSON.toJSONString(
                jpaQueryFactory.<JPAQueryFactory>get()
                        .select(
                                q.deleted,
                                Expressions.stringTemplate("group_concat({0})", q.id).as("ids")
                        )
                        .from(q)
                        .groupBy(q.deleted)
                        .fetch()
                        .stream()
                        .map(Tuple::toArray)
                        .collect(Collectors.toList())
        ));
        System.out.println("ifnull:" + JSON.toJSONString(
                jpaQueryFactory.<JPAQueryFactory>get()
                        .select(Projections.bean(Ifnull.class,
                                q.id
                                , Expressions.stringTemplate("ifnull({0}, {1})", q.content, "content is null").as("string")
                                , Expressions.numberTemplate(Double.class, "ifnull({0}, {1})", q.amount, 0D).as("amount")
                                , Expressions.numberTemplate(Double.class, "ifnull({0}, {1})", q.amount, q.id).as("otherColumn")
                        ))
                        .from(q)
                        .where(q.id.eq(1L))
                        .fetch()
        ));
        System.out.println("if:" + JSON.toJSONString(
                jpaQueryFactory.<JPAQueryFactory>get()
                        .select(
                                Expressions.stringTemplate("if({0}, {1}, {2})", q.content.isNull(), Expressions.constant("content is null"), Expressions.constant("content not null"))
                        )
                        .from(q)
                        .where(q.id.eq(1L))
                        .fetch()
        ));
//        System.out.println("countif:" + JSON.toJSONString(
//                jpaQueryFactory.<JPAQueryFactory>get()
//                        .select(
//                                Expressions.numberTemplate(Long.class, "countif({0}, {1}, {2})", q.id.lt(5), 1, Expressions.nullExpression())
//                        )
//                        .from(q)
//                        .groupBy(q.deleted)
//                        .fetch()
//        ));

    }

    @Data
    class Ifnull {
        private Long id;
        private String string;
        private Double amount;
        private Double otherColumn;
    }

}
