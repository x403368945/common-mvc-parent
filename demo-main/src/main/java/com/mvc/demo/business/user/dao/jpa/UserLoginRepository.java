package com.mvc.demo.business.user.dao.jpa;

import com.mvc.demo.business.user.entity.QTabUser;
import com.mvc.demo.business.user.entity.QTabUserLogin;
import com.mvc.demo.business.user.entity.TabUser;
import com.mvc.demo.business.user.entity.TabUserLogin;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.mvc.dao.IRepository;
import com.support.mvc.entity.base.Pager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.mvc.demo.business.user.entity.QTabUser.tabUser;
import static com.mvc.demo.business.user.entity.QTabUserLogin.tabUserLogin;
import static com.mvc.demo.config.init.BeanInitializer.Beans.jpaQueryFactory;


/**
 * 数据操作：用户登录记录表
 *
 *
 * @author 谢长春 on 2017/10/26
 */
public interface UserLoginRepository extends
        JpaRepository<TabUserLogin, Long>,
        IRepository<TabUserLogin, Long> {
    QTabUserLogin q = tabUserLogin;

    @Query
    @Override
    default QueryResults<TabUserLogin> findPage(final TabUserLogin condition, final Pager pager) {
//        Projections.bean(TabUser.class, q.id, q.openId, q.phone, q.nickname, q.role)
//
//        .where(JPAExpressions.selectFrom(q)
//                .where(q.groupId.eq(groupId)
//                        .and(q.workspaceId.eq(w.id))
//                        .and(q.userId.eq(userId))
//                        .and(q.role.eq(WorkspaceRole.Admin))
//                )
//                .exists()
//        .and(q.max.loe(JPAExpressions.select(d.fingerprint.countDistinct()).from(d).where(d.activityId.eq(q.id))
//        )
//
//        .map(tuple -> {
//            TabUser user = tuple.get(0, TabUser.class);
//            user.setGroupId(tuple.get(g.groupId));
//            return user;
//        });
//        jpaQueryFactory.<JPAQueryFactory>get()
//                .select(Projections.bean(
//                        q,
//                        Projections.bean(TabUser.class, user.id, user.username, user.nickname, user.role, user.deleted).as("user"))
//                );
        final QTabUser user = tabUser;
        final QueryResults<Tuple> results = jpaQueryFactory.<JPAQueryFactory>get()
                .select(q, Projections.bean(TabUser.class, user.id, user.username, user.nickname, user.role, user.deleted))
                .from(q)
                .leftJoin(user).on(user.id.eq(q.userId))
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
        return new QueryResults<>(
                results.getResults()
                        .stream()
                        .map(tuple -> Optional
                                .ofNullable(tuple.get(0, TabUserLogin.class))
                                .map(o -> {
                                    o.setUser(tuple.get(1, TabUser.class));
                                    return o;
                                })
                                .orElse(null)
                        )
                        .collect(Collectors.toList())
                ,
                results.getLimit(),
                results.getOffset(),
                results.getTotal()
        );
    }
}