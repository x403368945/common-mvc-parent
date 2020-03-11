package com.ccx.demo.business.user.dao.jpa;

import com.ccx.demo.business.user.cache.ITabRoleCache;
import com.ccx.demo.business.user.entity.QTabRole;
import com.ccx.demo.business.user.entity.TabRole;
import com.ccx.demo.enums.Bool;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.mvc.dao.IRepository;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

import static com.ccx.demo.config.init.BeanInitializer.Beans.jpaQueryFactory;
/**
 * 数据操作：角色表
 *
 * @author 谢长春 on 2019-08-29
 */
public interface RoleRepository extends
        JpaRepository<TabRole, Long>,
        IRepository<TabRole, Long> {
    // 每个 DAO 层顶部只能有一个查询实体,且必须以 q 命名,表示当前操作的数据库表. 当 q 作为主表的连接查询方法也必须写在这个类
    QTabRole q = QTabRole.tabRole;
//    /**
//     * 如果该表有缓存时请使用缓存，将这段代码注释，然后组合缓存接口。
//     * 组合模式：定义表数据无缓存时，优化连表查询方法.
//     * 实体类只需要组合该接口就可以获得按 id 查询方法.
//     * 这种实现方式可以分解连表查询，减轻数据库压力，对分页查询有优化，让单表查询方法复用范围更广
//     * 使用参考：
//     * <pre>
//     * public class TabEntity implements IRoleRepository{
//     *     public Long foreignKey;
//     *     public Set<Long> foreignKeys;
//     *
//     *     public TabRole getForeign(){
//     *         // 连表查询单条记录
//     *         return getTabRoleById(foreignKey).orElse(null);
//     *     }
//     *     public List<TabRole> getForeigns(){
//     *         // 连表查询多条记录
//     *         return getTabRoleByIds(foreignKeys);
//     *     }
//     * }
//     * </pre>
//     */
//    interface IRoleRepository {
//        /**
//         * 按 ID 获取数据行，用于表数据无缓存时，优化连表查询
//         *
//         * @param id {@link TabRole#getId()}
//         * @return {@link Optional<TabRole>}
//         */
//        @JSONField(serialize = false, deserialize = false)
//        default Optional<TabRole> getTabRoleById(final Long id) {
//            return getAppContext().getBean(RoleRepository.class).findById(id);
//        }
//
//        /**
//         * 按 ID 获取数据行，用于表数据无缓存时，优化连表查询
//         *
//         * @param ids {@link TabRole#getId()}
//         * @return {@link List<TabRole>}
//         */
//        @JSONField(serialize = false, deserialize = false)
//        default List<TabRole> getTabRoleByIds(final Set<Long> ids) {
//            return Lists.newArrayList(getAppContext().getBean(RoleRepository.class).findAll(q.id.in(ids)));
//        }
//    }

    @CacheEvict(cacheNames = ITabRoleCache.CACHE_ROW_BY_ID, key = "#id")
    @Override
    default long update(final Long id, final Long userId, final TabRole obj) {
        return obj.update(jpaQueryFactory.<JPAQueryFactory>get().update(q))
                .get()
                .where(q.id.eq(id).and(q.uid.eq(obj.getUid())).and(q.updateTime.eq(obj.getUpdateTime())))
                .execute();
    }

/*
//     @CacheEvict(cacheNames = ITabRoleCache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码 <
    @Override
    default TabRole deleteById(final Long id, final Long userId) {
        return Optional
                .ofNullable(jpaQueryFactory.<JPAQueryFactory>get()
                        .selectFrom(q)
                        .where(q.id.eq(id))
                        .fetchOne()
                )
                .map(obj -> {
                    delete(obj);
                    return obj;
                })
                .orElseThrow(() -> new NullPointerException("数据物理删除失败：".concat(
                        TabRole.builder().id(id).insertUserId(userId).build().json())
                ));
    }
*/

/*
//     @CacheEvict(cacheNames = ITabRoleCache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码 <
    @Override
    default TabRole deleteByUid(final Long id, final String uid, final Long userId) {
        // userId 为可选校验，一般业务场景，能获取到 UUID 已经表示已经加强校验了
        return Optional
                .ofNullable(jpaQueryFactory.<JPAQueryFactory>get()
                        .selectFrom(q)
                        .where(q.id.eq(id).and(q.uid.eq(uid)))
                        .fetchOne()
                )
                .map(obj -> {
                    delete(obj);
                    return obj;
                })
                .orElseThrow(() -> new NullPointerException("数据物理删除失败：".concat(
                        TabRole.builder().id(id).uid(uid).insertUserId(userId).build().json())
                ));
    }
*/


//     @CacheEvict(cacheNames = ITabRoleCache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码 <
    @Override
    default long markDeleteById(final Long id, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Bool.YES)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id).and(q.deleted.eq(Bool.NO)))
                .execute();
    }


//     @CacheEvict(cacheNames = ITabRoleCache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码 <
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
                .where(q.id.in(ids).and(q.deleted.eq(Bool.NO)))
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

    @Cacheable(cacheNames = ITabRoleCache.CACHE_ROW_BY_ID, key = "#id")
    default TabRole findCacheById(final Long id){
        return findById(id).orElse(null);
    }

    @Override
    default List<TabRole> findList(final TabRole condition) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }

    @Override
    default List<TabRole> findList(final TabRole condition, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(TabRole.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }

    @Override
    default QueryResults<TabRole> findPage(final TabRole condition, final Pager pager) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }

    @Override
    default QueryResults<TabRole> findPage(final TabRole condition, final Pager pager, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(TabRole.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }

    @Override
    default <T extends TabRole> List<T> findListProjection(final TabRole condition, final Class<T> clazz) {
        return findListProjection(condition, clazz, TabRole.allColumnAppends());
    }

    @Override
    default <T extends TabRole> List<T> findListProjection(final TabRole condition, final Class<T> clazz, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(clazz, exps))
                .from(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }

    @Override
    default <T extends TabRole> QueryResults<T> findPageProjection(final TabRole condition, final Pager pager, final Class<T> clazz) {
        return findPageProjection(condition, pager, clazz, TabRole.allColumnAppends());
    }

    @Override
    default <T extends TabRole> QueryResults<T> findPageProjection(final TabRole condition, final Pager pager, final Class<T> clazz, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(clazz, exps))
                .from(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }

//
//    /**
//     * 查询角色
//     *
//     * @return {@link String} 角色名称 {@link TabRole#getName()}
//     */
//    @Cacheable(cacheNames = ITabRoleCache.ROW, key = "#id")
//    default TabRole findCacheRoleById(final Long id) {
//        return findById(id).orElse(null);
//    }

//    /**
//     * 查询有效的角色集合，需要完全匹配 id:uid
//     *
//     * @param roles {@link Set<TabRole>} 角色集合
//     * @return {@link Set<Long>}
//     */
//    default Set<Long> findValidRoleIds(final Set<TabRole> roles) {
//        final Set<String> roleKeys = roles.stream()
//                // 拼接 id:uid
//                .map(row -> StringUtils.joinWith(":", row.getId(), row.getUid()))
//                .collect(Collectors.toSet());
//        return jpaQueryFactory.<JPAQueryFactory>get()
//                .select(Projections.bean(TabRole.class, q.id, q.uid))
//                .from(q)
//                .where(q.id.in(roles.stream().map(TabRole::getId).toArray(Long[]::new)).and(q.deleted.eq(Bool.NO)))
//                .fetch()
//                .stream()
//                // 过滤有效的角色
//                .filter(row -> roleKeys.contains(StringUtils.joinWith(":", row.getId(), row.getUid())))
//                .map(TabRole::getId)
//                .collect(Collectors.toSet())
//                ;
//    }
}
