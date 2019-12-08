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
import org.springframework.data.domain.Example;
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

//    @Override
//    default long update(final <%=id%> id, final <%=TabName%> obj) {
//        return obj.update(jpaQueryFactory.<JPAQueryFactory>get().update(q))
//        .get()
//        .where(q.id.eq(id).and(q.uid.eq(obj.getUid())).and(q.modifyTime.eq(obj.getModifyTime())))
//        .execute();
//    }

    @Override
    default Optional<<%=TabName%>> findByUid(final <%=id%> id, final String uid) {
        return findOne(
                q.id.eq(id).and(q.uid.eq(uid))
        );
    }
//
//    @Override
//    default <%=TabName%> deleteByUid(final <%=id%> id, final String uid) {
//        // 只能删除自己创建的数据，且使用 UUID 强校验；
//        return Optional
//                .ofNullable(jpaQueryFactory.<JPAQueryFactory>get()
//                        .selectFrom(q)
//                        .where(q.id.eq(id).and(q.uid.eq(uid)))
//                        .fetchOne()
//                )
//                .map(obj -> {
//                    delete(obj);
//                    return obj;
//                })
//                .orElseThrow(() -> new NullPointerException("数据物理删除失败：".concat(
//                        <%=TabName%>.builder().id(id).uid(uid)..build().json())
//                ));
//    }
//
//    @Override
//    default long markDeleteById(final <%=id%> id) {
//        return jpaQueryFactory.<JPAQueryFactory>get()
//                .update(q)
//                .set(q.deleted, Radio.YES)
//                .where(q.id.eq(id).and(q.deleted.eq(Radio.NO)))
//                .execute();
//    }

//    @Override
//    default long markDeleteByUid(final <%=id%> id, final String uid) {
//        return jpaQueryFactory.<JPAQueryFactory>get()
//        .update(q)
//        .set(q.deleted, Radio.YES)
//        .where(q.id.eq(id).and(q.uid.eq(uid).and(q.deleted.eq(Radio.NO))))
//        .execute();
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
//        .update(q)
//        .set(q.deleted, Radio.YES)
//        .where(q.id.in(list.stream().map(<%=TabName%>::getId).toArray(Long[]::new))
//        .and(q.deleted.eq(Radio.NO))
//        .and(q.uid.in(list.stream().map(<%=TabName%>::getUid).toArray(String[]::new)))
//        )
//        .execute();
//    }
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
