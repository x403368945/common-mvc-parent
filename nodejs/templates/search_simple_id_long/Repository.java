package <%=pkg%>.code.<%=javaname%>.dao.jpa;

import <%=pkg%>.code.<%=javaname%>.entity.<%=TabName%>;
import <%=pkg%>.code.<%=javaname%>.entity.Q<%=TabName%>;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.mvc.dao.ISearchRepository;
import com.support.mvc.entity.base.Pager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static <%=pkg%>.config.init.BeanInitializer.Beans.jpaQueryFactory;

/**
 * 数据操作：<%=comment%>
 *
 * @author 谢长春 on <%=date%>
 */
public interface <%=JavaName%>Repository extends
        JpaRepository<<%=TabName%>, <%=id%>>,
        ISearchRepository<<%=TabName%>> {
    Q<%=TabName%> q = Q<%=TabName%>.<%=tabName%>;

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
