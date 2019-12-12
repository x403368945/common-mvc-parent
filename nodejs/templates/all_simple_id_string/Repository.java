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
//    @Override
//    default long update(final <%=id%> id, final <%=TabName%> obj) {
//        return obj.update(jpaQueryFactory.<JPAQueryFactory>get().update(q))
//        .get()
//        .where(q.id.eq(id).and(q.updateTime.eq(obj.getUpdateTime())))
//        .execute();
//    }

//     @CacheEvict(cacheNames = I<%=TabName%>Cache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码
//    @Override
//    default long markDeleteById(final <%=id%> id) {
//        return jpaQueryFactory.<JPAQueryFactory>get()
//                .update(q)
//                .set(q.deleted, Radio.YES)
//                .where(q.id.eq(id).and(q.deleted.eq(Radio.NO)))
//                .execute();
//    }

//    @Override
//    default long markDeleteByIds(final List<<%=id%>> ids) {
//        return jpaQueryFactory.<JPAQueryFactory>get()
//                .update(q)
//                .set(q.deleted, Radio.YES)
//                .where(q.id.in(ids).and(q.deleted.eq(Radio.NO)))
//                .execute();
//    }

//    @Override
//    default long markDelete(final List<<%=TabName%>> list) {
//        return jpaQueryFactory.<JPAQueryFactory>get()
//                   .update(q)
//                   .set(q.deleted, Radio.YES)
//                   .where(q.id.in(list.stream().map(<%=TabName%>::getId).toArray(String[]::new))
//                       .and(q.deleted.eq(Radio.NO))
//                    )
//                    .execute();
//    }

//     @Cacheable(cacheNames = I<%=TabName%>Cache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码
//     default <%=TabName%> findCacheById(final <%=id%> id){
//         return findById(id).orElse(null);
//     }

//    @Override
//    default List<<%=TabName%>> findList(final <%=TabName%> condition) {
//        return jpaQueryFactory.<JPAQueryFactory>get()
//                .selectFrom(q)
//                .where(condition.where().toArray())
//                .orderBy(condition.buildQdslSorts())
//                .fetch();
//    }
//
//    @Override
//    default List<<%=TabName%>> findList(final <%=TabName%> condition, final Expression<?>... exps) {
//        return jpaQueryFactory.<JPAQueryFactory>get()
//                .select(Projections.bean(<%=TabName%>.class, exps))
//                .from(q)
//                .where(condition.where().toArray())
//                .orderBy(condition.buildQdslSorts())
//                .fetch();
//    }

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
}
