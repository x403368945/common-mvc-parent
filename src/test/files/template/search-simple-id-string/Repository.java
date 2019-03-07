package com.ccx.business.{javaname}.dao.jpa;

import com.ccx.business.{javaname}.entity.{TabName};
import com.ccx.business.{javaname}.entity.Q{TabName};
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.mvc.dao.ISearchRepository;
import com.support.mvc.entity.base.Pager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static com.ccx.demo.config.init.BeanInitializer.Beans.jpaQueryFactory;

/**
 * 数据操作：{comment}
 *
 * @author 谢长春 on {date}
 */
public interface {JavaName}Repository extends
        JpaRepository<{TabName}, {ID}>,
        ISearchRepository<{TabName}> {
    Q{TabName} q = Q{TabName}.{tabName};

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
