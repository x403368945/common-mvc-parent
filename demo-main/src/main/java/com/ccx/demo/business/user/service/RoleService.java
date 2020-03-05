package com.ccx.demo.business.user.service;

import com.ccx.demo.business.user.cache.ITabRoleCache;
import com.ccx.demo.business.user.dao.jpa.RoleRepository;
import com.ccx.demo.business.user.entity.QTabRole;
import com.ccx.demo.business.user.entity.TabRole;
import com.ccx.demo.business.user.vo.Authority;
import com.ccx.demo.enums.Bool;
import com.querydsl.core.QueryResults;
import com.support.aop.annotations.ServiceAspect;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.exception.DeleteRowsException;
import com.support.mvc.exception.UpdateRowsException;
import com.support.mvc.service.IService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.ccx.demo.config.init.BeanInitializer.Beans.cacheManager;

/**
 * 服务接口实现类：角色表
 *
 * @author 谢长春 on 2019-08-29
 */
@Slf4j
@Service
@ServiceAspect
@RequiredArgsConstructor
public class RoleService implements IService<TabRole>
        , ITabRoleCache {
    private final RoleRepository repository;
    private final AuthorityService authorityService;

    /**
     * 获取当前缓存管理器，用于代码控制缓存
     *
     * @return {@link Cache}
     */
    public Cache getCacheManager() {
        return Objects.requireNonNull(cacheManager.<CacheManager>get().getCache(CACHE_ROW_BY_ID), "未获取到缓存管理对象:".concat(CACHE_ROW_BY_ID));
    }

    /**
     * 清除多个 key 对应的缓存
     *
     * @param ids {@link TabRole#getId()}
     */
    public void clearKeys(final Collection<Long> ids) {
        ids.stream().distinct().forEach(id -> getCacheManager().evict(id));

    }

    @Override
    public TabRole save(final TabRole obj, final Long userId) {
        final String[] authorities = authorityService.expendFilterCheckedSet(obj.getAuthorityTree())
                .stream()
                .map(Authority::getCode)
                .map(Enum::name)
                .toArray(String[]::new);
        Assert.notEmpty(authorities, "权限指令树【authorityTree】配置无效，有效的权限指令代码不能为空");
        obj.setAuthorities(authorities);
        return repository.save(obj);
    }

    @Override
    public void update(final Long id, final Long userId, final TabRole obj) {
        final String[] authorities = authorityService.expendFilterCheckedSet(obj.getAuthorityTree())
                .stream()
                .map(Authority::getCode)
                .map(Enum::name)
                .toArray(String[]::new);
        Assert.notEmpty(authorities, "权限指令树【authorityTree】配置无效，有效的权限指令代码不能为空");
        obj.setAuthorities(authorities);
        UpdateRowsException.asserts(repository.update(id, userId, obj));
    }

    @Override
    public void markDeleteByUid(final Long id, final String uid, final Long userId) {
        DeleteRowsException.asserts(repository.markDeleteByUid(id, uid, userId));
    }

    @Override
    public void markDelete(final List<MarkDelete> list, final Long userId) {
        DeleteRowsException.warn(repository.markDelete(list, userId), list.size());
        clearKeys(list.stream().map(MarkDelete::getLongId).collect(Collectors.toSet()));
    }

    @Override
    public Optional<TabRole> findByUid(final Long id, final String uid) {
        return Optional.ofNullable(repository.findCacheById(id)).filter(row -> Objects.equals(uid, row.getUid()));
    }

    @Override
    public QueryResults<TabRole> findPage(final TabRole condition, final Pager pager) {
        return repository.findPage(condition, Pager.rebuild(pager));
    }

    /**
     * 匹配有效的角色id
     *
     * @param roles {@link List<TabRole>} 角色集合
     * @return {@link List<Long>}
     */
    public @NotEmpty(message = "matchValidRoleIds:返回值不能为空集合") Set<Long> matchValidRoleIds(
            @NotEmpty(message = "【ids】不能为空") final Set<TabRole> roles) {
//        return repository.findValidRoleIds(roles);
        final Set<String> roleKeys = roles.stream()
                // 拼接 id:uid
                .map(row -> StringUtils.joinWith(":", row.getId(), row.getUid()))
                .collect(Collectors.toSet());
        return roles.stream()
                // 从缓存获取数据
                .map(role -> getTabRoleCacheById(role.getId()).orElse(null))
                // 排除已删除的角色
                .filter(role -> Objects.nonNull(role) && Objects.equals(Bool.NO, role.getDeleted()))
                // 过滤有效的角色
                .filter(role -> roleKeys.contains(StringUtils.joinWith(":", role.getId(), role.getUid())))
                .map(TabRole::getId)
                .collect(Collectors.toSet())
                ;
    }

    /**
     * 获取前端角色选项集合
     *
     * @return {@link List<TabRole>}
     */
    public List<TabRole> getOptions() {
        final QTabRole q = QTabRole.tabRole;
        return repository.findList(
                TabRole.builder().deleted(Bool.NO).build(),
                q.id,
                q.uid,
                q.name
        );
    }
}
