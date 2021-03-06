package com.ccx.demo.business.user.dao.jpa;

import com.ccx.demo.business.user.cache.ITabUserCache;
import com.ccx.demo.business.user.entity.QTabUser;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.enums.Bool;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.mvc.dao.IRepository;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.utils.util.Op;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ccx.demo.config.init.BeanInitializer.Beans.jpaQueryFactory;

/**
 * 数据操作：用户表
 *
 * @author 谢长春 on 2017/10/26
 */
public interface UserRepository extends
        JpaRepository<TabUser, Long>,
        IRepository<TabUser, Long> {
    // 每个 DAO 层顶部只能有一个查询实体,且必须以 q 命名,表示当前操作的数据库表. 当 q 作为主表的连接查询方法也必须写在这个类
    QTabUser q = QTabUser.tabUser;

//    /**
//     * 如果该表有缓存时请使用缓存，将这段代码注释，然后组合缓存接口。
//     * 组合模式：定义表数据无缓存时，优化连表查询方法.
//     * 实体类只需要组合该接口就可以获得按 id 查询方法.
//     * 这种实现方式可以分解连表查询，减轻数据库压力，对分页查询有优化，让单表查询方法复用范围更广
//     * 使用参考：
//     * <pre>
//     * public class TabEntity implements IUserRepository{
//     *     public Long foreignKey;
//     *     public Set<Long> foreignKeys;
//     *
//     *     public TabUser getForeign(){
//     *         // 连表查询单条记录
//     *         return getTabUserById(foreignKey).orElse(null);
//     *     }
//     *     public List<TabUser> getForeigns(){
//     *         // 连表查询多条记录
//     *         return getTabUserByIds(foreignKeys);
//     *     }
//     * }
//     * </pre>
//     */
//    interface IUserRepository {
//        /**
//         * 按 ID 获取数据行，用于表数据无缓存时，优化连表查询
//         *
//         * @param id {@link TabUser#getId()}
//         * @return {@link Optional<TabUser>}
//         */
//        @JSONField(serialize = false, deserialize = false)
//        default Optional<TabUser> getTabUserById(final Long id) {
//            return getAppContext().getBean(UserRepository.class).findById(id);
//        }
//
//        /**
//         * 按 ID 获取数据行，用于表数据无缓存时，优化连表查询
//         *
//         * @param ids {@link TabUser#getId()}
//         * @return {@link List<TabUser>}
//         */
//        @JSONField(serialize = false, deserialize = false)
//        default List<TabUser> getTabUserByIds(final Set<Long> ids) {
//            return Lists.newArrayList(getAppContext().getBean(UserRepository.class).findAllById(ids));
//        }
//    }

    @Override
    default long update(final Long id, final Long userId, final TabUser obj) {
        return obj.update(jpaQueryFactory.<JPAQueryFactory>get().update(q))
                .get()
                .where(q.id.eq(id).and(q.uid.eq(obj.getUid())).and(q.deleted.eq(Bool.NO)))
                .execute();
    }

    @Override
    default long markDeleteById(final Long id, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Bool.YES)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id))
                .execute();
    }


    //     @CacheEvict(cacheNames = ITabUserCache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码 <
    @Override
    default long markDeleteByUid(final Long id, final String uid, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Bool.YES)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id).and(q.uid.eq(uid)).and(q.deleted.eq(Bool.NO)))
                .execute();
    }


    @Override // <
    default long markDeleteByIds(final Set<Long> ids, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Bool.YES)
                .set(q.updateUserId, userId)
                .where(q.id.in(ids))
                .execute();
    }

    @Override // <
    default long markDelete(final List<MarkDelete> list, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Bool.YES)
                .set(q.updateUserId, userId)
                .where(q.id.in(list.stream().map(MarkDelete::getLongId).toArray(Long[]::new))
                        .and(q.deleted.eq(Bool.NO))
                        .and(q.uid.in(list.stream().map(MarkDelete::getUid).toArray(String[]::new)))
                )
                .execute();
    }

    @Cacheable(cacheNames = ITabUserCache.CACHE_ROW_BY_ID, key = "#id")
    default TabUser findCacheById(final Long id) {
        return findById(id).orElse(null);
    }

    @Override // <
    default List<TabUser> findList(final TabUser condition) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }


    @Override // <
    default List<TabUser> findList(final TabUser condition, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(TabUser.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }


    @Override // <
    default QueryResults<TabUser> findPage(final TabUser condition, final Pager pager) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }

    @Override // <
    default QueryResults<TabUser> findPage(final TabUser condition, final Pager pager, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(TabUser.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }


    @Override // <
    default <T extends TabUser> List<T> findListProjection(final TabUser condition, final Class<T> clazz) {
        return findListProjection(condition, clazz, TabUser.allColumnAppends());
    }


    @Override // <
    default <T extends TabUser> List<T> findListProjection(final TabUser condition, final Class<T> clazz, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(clazz, exps))
                .from(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }


    @Override // <
    default <T extends TabUser> QueryResults<T> findPageProjection(final TabUser condition, final Pager pager, final Class<T> clazz) {
        return findPageProjection(condition, pager, clazz, TabUser.allColumnAppends());
    }


    @Override // <
    default <T extends TabUser> QueryResults<T> findPageProjection(final TabUser condition, final Pager pager, final Class<T> clazz, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(clazz, exps))
                .from(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }
//    /**
//     * 查询部分字段，这些字段会被缓存到 redis
//     *
//     * @return List<TabUser>
//     */
//    default List<TabUser> getSimpleList() {
//        return jpaQueryFactory.<JPAQueryFactory>get()
//                .select(Projections.bean(TabUser.class, q.id, q.uid, q.username, q.nickname, q.phone, q.email, q.roles))
//                .from(q)
//                .fetch();
////                .stream()
////                .map(row -> TabUser.builder()
////                        .id(row.get(q.id))
////                        .domain(row.get(q.domain))
////                        .username(row.get(q.username))
////                        .phone(row.get(q.phone))
////                        .email(row.get(q.email))
////                        .nickname(row.get(q.nickname))
////                        .role(row.get(q.role))
////                        .build()
////                ).collect(Collectors.toList());
//    }

    /**
     * 按【登录账户、手机号、邮箱】查找用户
     *
     * @param username String 登录账户名
     * @return Optional<TabUser>
     */
    @Cacheable(cacheNames = ITabUserCache.CACHE_LOGIN, key = "#username")
    default Optional<TabUser> findUser(final String username) {
        return Op.of(findOne(q.username.eq(username)))
                .orElseOf(() -> findOne(q.phone.eq(username)))
                .orElseOf(() -> findOne(q.email.eq(username)))
                .optional();
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
//                .set(q.deleted, Bool.NO)
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
//                .set(q.expired, Bool.NO)
//                .set(q.updateUserId, userId)
//                .where(q.id.eq(id))
//                .execute();
//    }

}
