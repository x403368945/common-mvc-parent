package <%=pkg%>.code.<%=javaname%>.dao.jpa;

import <%=pkg%>.code.<%=javaname%>.entity.<%=TabName%>;
import <%=pkg%>.code.<%=javaname%>.entity.Q<%=TabName%>;
import <%=pkg%>.enums.Radio;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.mvc.dao.IRepository;
import com.support.mvc.entity.base.Pager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static <%=pkg%>.config.init.BeanInitializer.Beans.jpaQueryFactory;

/**
 * 数据操作：<%=comment%>
 *
 * @author 谢长春 on <%=date%>
 */
public interface <%=JavaName%>Repository extends
        JpaRepository<<%=TabName%>, <%=id%>>,
        IRepository<<%=TabName%>, <%=id%>> {
    Q<%=TabName%> q = Q<%=TabName%>.<%=tabName%>;

//     @CacheEvict(cacheNames = I<%=TabName%>Cache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码
    @Override
    default long update(final <%=id%> id, final Long userId, final <%=TabName%> obj) {
        return obj.update(jpaQueryFactory.<JPAQueryFactory>get().update(q))
                .get()
                .where(q.id.eq(id).and(q.uid.eq(obj.getUid())).and(q.insertUserId.eq(userId)).and(q.updateTime.eq(obj.getUpdateTime())))
                .execute();
    }
//     @CacheEvict(cacheNames = I<%=TabName%>Cache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码
    @Override
    default <%=TabName%> deleteById(final <%=id%> id, final Long userId) {
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
                        <%=TabName%>.builder().id(id).insertUserId(userId).build().json())
                ));
    }

//     @CacheEvict(cacheNames = I<%=TabName%>Cache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码
    @Override
    default <%=TabName%> deleteByUid(final <%=id%> id, final String uid, final Long userId) {
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
                        <%=TabName%>.builder().id(id).uid(uid).insertUserId(userId).build().json())
                ));
    }

//     @CacheEvict(cacheNames = I<%=TabName%>Cache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码
    @Override
    default long markDeleteById(final <%=id%> id, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id).and(q.insertUserId.eq(userId)).and(q.deleted.eq(Radio.NO)))
                .execute();
    }

//     @CacheEvict(cacheNames = I<%=TabName%>Cache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码
    @Override
    default long markDeleteByUid(final <%=id%> id, final String uid, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id).and(q.uid.eq(uid).and(q.insertUserId.eq(userId))).and(q.deleted.eq(Radio.NO)))
                .execute();
    }

    @Override
    default long markDeleteByIds(final List<<%=id%>> ids, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.in(ids).and(q.insertUserId.eq(userId)).and(q.deleted.eq(Radio.NO)))
                .execute();
    }

    @Override
    default long markDelete(final List<<%=TabName%>> list, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.in(list.stream().map(<%=TabName%>::getId).toArray(Long[]::new))
                        .and(q.insertUserId.eq(userId)).and(q.deleted.eq(Radio.NO))
                        .and(q.uid.in(list.stream().map(<%=TabName%>::getUid).toArray(String[]::new)))
                )
                .execute();
    }

//     @Cacheable(cacheNames = I<%=TabName%>Cache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码
//     default <%=TabName%> findCacheById(final <%=id%> id){
//         return findById(id).orElse(null);
//     }

    @Override
    default List<<%=TabName%>> findList(final <%=TabName%> condition) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }

    @Override
    default List<<%=TabName%>> findList(final <%=TabName%> condition, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(<%=TabName%>.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }

    @Override
    default QueryResults<<%=TabName%>> findPage(final <%=TabName%> condition, final Pager pager) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }


    @Override
    default QueryResults<<%=TabName%>> findPage(final <%=TabName%> condition, final Pager pager, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(<%=TabName%>.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }

    @Override
    default <T extends <%=TabName%>> List<T> findListProjection(final <%=TabName%> condition, final Class<T> clazz) {
        return findListProjection(condition, clazz, <%=TabName%>.allColumns());
    }

    @Override
    default <T extends <%=TabName%>> List<T> findListProjection(final <%=TabName%> condition, final Class<T> clazz, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(clazz, exps))
                .from(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }

    @Override
    default <T extends <%=TabName%>> QueryResults<T> findPageProjection(final <%=TabName%> condition, final Pager pager, final Class<T> clazz) {
        return findPageProjection(condition, pager, clazz, <%=TabName%>.allColumns());
    }

    @Override
    default <T extends <%=TabName%>> QueryResults<T> findPageProjection(final <%=TabName%> condition, final Pager pager, final Class<T> clazz, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(clazz, exps))
                .from(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }

}
