package com.ccx.demo.business.user.dao.jpa;

import com.ccx.demo.business.user.cache.ITabRoleCache;
import com.ccx.demo.business.user.entity.QTabRole;
import com.ccx.demo.business.user.entity.TabRole;
import com.ccx.demo.enums.Radio;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.mvc.dao.IRepository;
import com.support.mvc.entity.base.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ccx.demo.config.init.BeanInitializer.Beans.jpaQueryFactory;

/**
 * 数据操作：角色表
 *
 * @author 谢长春 on 2019-08-29
 */
public interface RoleRepository extends
        JpaRepository<TabRole, Long>,
        IRepository<TabRole, Long> {
    QTabRole q = QTabRole.tabRole;

    @CacheEvict(cacheNames = ITabRoleCache.CACHE_ROW_BY_ID, key = "#id")
    @Override
    default long update(final Long id, final Long userId, final TabRole obj) {
        return findById(id)
                .filter(dest -> Objects.equals(obj.getUid(), dest.getUid()))
                .filter(dest -> Objects.equals(obj.getUpdateTime(), dest.getUpdateTime()))
                .map(dest -> {
                    obj.update(dest);
                    return 1L;
                })
                .orElse(0L);
    }

    @CacheEvict(cacheNames = ITabRoleCache.CACHE_ROW_BY_ID, key = "#id")
    @Override
    default long markDeleteByUid(final Long id, final String uid, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.eq(id).and(q.uid.eq(uid)).and(q.deleted.eq(Radio.NO)))
                .execute();
    }

    @Override
    default long markDelete(final List<TabRole> list, final Long userId) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Radio.YES)
                .set(q.updateUserId, userId)
                .where(q.id.in(list.stream().map(TabRole::getId).toArray(Long[]::new))
                        .and(q.insertUserId.eq(userId)).and(q.deleted.eq(Radio.NO))
                        .and(q.uid.in(list.stream().map(TabRole::getUid).toArray(String[]::new)))
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

    /**
     * 查询有效的角色集合，需要完全匹配 id:uid
     *
     * @param roles {@link List<TabRole>} 角色集合
     * @return {@link List<Long>}
     */
    default Set<Long> findValidRoleIds(final List<TabRole> roles) {
        final Set<String> roleKeys = roles.stream()
                // 拼接 id:uid
                .map(row -> StringUtils.joinWith(":", row.getId(), row.getUid()))
                .collect(Collectors.toSet());
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(TabRole.class, q.id, q.uid))
                .from(q)
                .where(q.id.in(roles.stream().map(TabRole::getId).toArray(Long[]::new)).and(q.deleted.eq(Radio.NO)))
                .fetch()
                .stream()
                // 过滤有效的角色
                .filter(row -> roleKeys.contains(StringUtils.joinWith(":", row.getId(), row.getUid())))
                .map(TabRole::getId)
                .collect(Collectors.toSet())
                ;
    }
}