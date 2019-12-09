package com.ccx.demo.business.user.dao.jpa;

import com.ccx.demo.business.user.entity.QTabUser;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.config.CacheConfig;
import com.ccx.demo.enums.Radio;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.aop.annotations.Master;
import com.support.mvc.dao.IRepository;
import com.support.mvc.entity.base.Pager;
import com.utils.util.Op;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import static com.ccx.demo.business.user.entity.QTabUser.tabUser;
import static com.ccx.demo.config.init.BeanInitializer.Beans.jpaQueryFactory;

/**
 * 数据操作：用户表
 *
 * @author 谢长春 on 2017/10/26
 */
public interface UserRepository extends
        JpaRepository<TabUser, Long>,
        IRepository<TabUser, Long> {
    QTabUser q = tabUser;


    @Cacheable(cacheNames = CacheConfig.nicknameCache, key = "#id")
    @Query
    default String getNickame(final Long id) {
        return jpaQueryFactory.<JPAQueryFactory>get().select(q.nickname).from(q).where(q.id.eq(id)).fetchOne();
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
    default long markDeleteByIds(final List<Long> ids, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.in(ids).and(q.insertUserId.eq(userId)))
                .execute();
    }

    //    @Query
//    @Override
//    default List<TabUser> findList( final TabUser condition) {
//        final QTabUser q = tabUser;
//        return Query.<JPAQueryFactory>get()
//                .selectFrom(q)
//                .where(formatWhere(q, condition))
//                .orderBy(Sorts.qdslOrder(OrderBy.id.desc, sorts))
//                .fetch();
//    }
    @Override
    default QueryResults<TabUser> findPage(final TabUser condition, final Pager pager) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }

    /**
     * 查询部分字段，这些字段会被缓存到 redis
     *
     * @return List<TabUser>
     */
    default List<TabUser> getSimpleList() {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(TabUser.class, q.id, q.uid, q.username, q.nickname, q.phone, q.email, q.role))
                .from(q)
                .fetch();
//                .stream()
//                .map(row -> TabUser.builder()
//                        .id(row.get(q.id))
//                        .subdomain(row.get(q.subdomain))
//                        .username(row.get(q.username))
//                        .phone(row.get(q.phone))
//                        .email(row.get(q.email))
//                        .nickname(row.get(q.nickname))
//                        .role(row.get(q.role))
//                        .build()
//                ).collect(Collectors.toList());
    }

    /**
     * 按【登录账户、手机号、邮箱】查找用户
     *
     * @param username String 登录账户名
     * @return Optional<TabUser>
     */
    @Master
    @Cacheable(cacheNames = CacheConfig.loginCache, key = "#username")
    default Optional<TabUser> findUser(final String username) {
        return Op.of(findOne(q.username.eq(username)))
                .orElseOf(() -> findOne(q.phone.eq(username)))
                .orElseOf(() -> findOne(q.email.eq(username)))
                .optional();
    }

    /**
     * 通知清除登录查询缓存，修改密码、修改昵称后都需要触发该方法，清除 {@link UserRepository#findUser(String)} 缓存，
     * 在 {@link UserRepository} 内方法之间相互调用无法触发 @CacheEvict 代理， 必须在 service 层触发，
     *
     * @param username String 登录账户名
     */
    @CacheEvict(cacheNames = CacheConfig.loginCache, key = "#username")
    default void clearLoginCache(final String username) {
    }

    /**
     * 修改密码
     *
     * @param id       String 用户ID
     * @param password String 新密码
     * @param userId   Long 修改者ID
     * @return long 影响行数
     */
    @Modifying
    @Query
    default long updatePassword(final Long id, final String password, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.password, password)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id))
                .execute();
    }

    /**
     * 修改昵称
     *
     * @param id       Long 用户ID
     * @param nickname String 昵称
     * @param userId   Long 修改者ID
     * @return long 影响行数
     */
    @CacheEvict(cacheNames = CacheConfig.nicknameCache, key = "#id")
    @Modifying
    @Query
    default long updateNickname(final Long id, final String nickname, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.nickname, nickname)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id))
                .execute();
    }

//    /**
//     * 启用账户
//     *
//     * @param id     Long 用户ID
//     * @param userId Long 修改者ID
//     * @return long 影响行数
//     */
//    @Modifying
//    @Query
//    default long enable(final Long id, final Long userId) {
//        return jpaQueryFactory.<JPAQueryFactory>get()
//                .update(q)
//                .set(q.deleted, Radio.NO)
//                .set(q.updateUserId, userId)
//                .where(q.id.eq(id))
//                .execute();
//    }
//
//    /**
//     * 激活账户
//     *
//     * @param id     Long 用户ID
//     * @param userId Long 修改者ID
//     * @return long 影响行数
//     */
//    @Modifying
//    @Query
//    default long activate(final Long id, final Long userId) {
//        return jpaQueryFactory.<JPAQueryFactory>get()
//                .update(q)
//                .set(q.expired, Radio.NO)
//                .set(q.updateUserId, userId)
//                .where(q.id.eq(id))
//                .execute();
//    }
}