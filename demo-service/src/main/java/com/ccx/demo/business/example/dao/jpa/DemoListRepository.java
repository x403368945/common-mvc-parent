package com.ccx.demo.business.example.dao.jpa;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.ccx.demo.business.example.entity.QTabDemoList;
import com.ccx.demo.business.example.entity.TabDemoList;
import com.ccx.demo.enums.Bool;
import com.google.common.collect.Lists;
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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ccx.demo.config.init.BeanInitializer.Beans.jpaQueryFactory;
import static com.ccx.demo.config.init.BeanInitializer.getAppContext;

/**
 * 数据操作：
 *
 * @author 谢长春 on 2018-12-17
 */
public interface DemoListRepository extends
        JpaRepository<TabDemoList, Long>,
        IRepository<TabDemoList, Long> {
    QTabDemoList q = QTabDemoList.tabDemoList;

    /**
     * 如果该表有缓存时请使用缓存，将这段代码注释，然后组合缓存接口。
     * 组合模式：定义表数据无缓存时，优化连表查询方法.
     * 实体类只需要组合该接口就可以获得按 id 查询方法.
     * 这种实现方式可以分解连表查询，减轻数据库压力，对分页查询有优化，让单表查询方法复用范围更广
     * 使用参考：
     * <pre>
     * public class TabEntity implements IDemoListRepository{
     *     public Long foreignKey;
     *     public Set<Long> foreignKeys;
     *
     *     public TabDemoList getForeign(){
     *         // 连表查询单条记录
     *         return getTabDemoListById(foreignKey).orElse(null);
     *     }
     *     public List<TabDemoList> getForeigns(){
     *         // 连表查询多条记录
     *         return getTabDemoListByIds(foreignKeys);
     *     }
     * }
     * </pre>
     */
    interface IDemoListRepository {
        /**
         * 按 ID 获取数据行，用于表数据无缓存时，优化连表查询
         *
         * @param id {@link TabDemoList#getId()}
         * @return {@link Optional<TabDemoList>}
         */
        @JSONField(serialize = false, deserialize = false)
        default Optional<TabDemoList> getTabDemoListById(final Long id) {
            return getAppContext().getBean(DemoListRepository.class).findById(id);
        }

        /**
         * 按 ID 获取数据行，用于表数据无缓存时，优化连表查询
         *
         * @param ids {@link TabDemoList#getId()}
         * @return {@link List<TabDemoList>}
         */
        @JSONField(serialize = false, deserialize = false)
        default List<TabDemoList> getTabDemoListByIds(final Collection<Long> ids) {
            return Lists.newArrayList(getAppContext().getBean(DemoListRepository.class).findAll(q.id.in(ids)));
        }
    }

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
                .set(q.deleted, Bool.YES)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id).and(q.insertUserId.eq(userId)))
                .execute();
    }

    @Override
    default long markDeleteByUid(final Long id, final String uid, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Bool.YES)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id).and(q.uid.eq(uid).and(q.insertUserId.eq(userId))))
                .execute();
    }

    @Override
    default long markDeleteByIds(final List<Long> ids, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Bool.YES)
                .set(q.updateUserId, userId)
                .where(q.id.in(ids).and(q.insertUserId.eq(userId)))
                .execute();
    }

    @Override
    default long markDelete(final List<TabDemoList> list, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Bool.YES)
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

    @Override
    default <T extends TabDemoList> List<T> findListProjection(final TabDemoList condition, final Class<T> clazz) {
        return findListProjection(condition, clazz, TabDemoList.allColumnAppends());
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
        return findPageProjection(condition, pager, clazz, TabDemoList.allColumnAppends());
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
