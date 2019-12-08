package com.ccx.demo.business.user.cache;

import com.alibaba.fastjson.annotation.JSONField;
import com.ccx.demo.business.user.dao.jpa.RoleRepository;
import com.ccx.demo.business.user.entity.TabRole;
import com.ccx.demo.enums.Radio;
import com.querydsl.core.annotations.QueryTransient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.CollectionUtils;

import java.beans.Transient;
import java.util.*;
import java.util.stream.Collectors;

import static com.ccx.demo.config.init.BeanInitializer.Beans.cacheManager;
import static com.ccx.demo.config.init.BeanInitializer.Beans.getAppContext;

/**
 * <pre>
 * 缓存角色权限
 *
 * @author 谢长春 2019/8/29
 */
public interface ITabRoleCache {
    String ROW = "ITabRoleCache";

    /**
     * 按 ID 获取数据缓存行
     *
     * @param id {@link TabRole#getId()}
     * @return {@link Optional<TabRole>}
     */
    @JSONField(serialize = false, deserialize = false)
    default Optional<TabRole> getTabRoleCacheById(final Long id) {
        return getAppContext().getBean(RoleRepository.class).findById(id);
    }

    /**
     * 从缓存获取角色权限
     *
     * @param id {@link Long} 角色ID
     * @return {@link List<String>} 角色权限集合，参考：{@link com.ccx.demo.business.user.enums.AuthorityCode}
     */
    @Transient
    @QueryTransient
    @JSONField(serialize = false, deserialize = false)
    default List<String> getRoleAuthoritiesByCacheId(final Long id) {
        if (Objects.isNull(id)) return Collections.emptyList();
        return getTabRoleCacheById(id)
                .filter(obj -> Objects.equals(obj.getDeleted(), Radio.NO))
                .map(TabRole::getAuthorities)
                .orElseGet(Collections::emptyList)
                ;
    }

    /**
     * 从缓存获取角色名称
     *
     * @param id {@link Long} 角色ID
     * @return {@link String} 角色名称，{@link TabRole#getName()}
     */
    @Transient
    @QueryTransient
    @JSONField(serialize = false, deserialize = false)
    default String getRoleNameByCacheId(final Long id) {
        if (Objects.isNull(id)) return null;
        return getTabRoleCacheById(id).map(TabRole::getName).orElse(null);
    }

    /**
     * 从缓存获取角色权限
     *
     * @param ids {@link Long} 角色 ID 集合
     * @return {@link List<String>} 角色权限集合，{@link TabRole#getName()}
     */
    @Transient
    @QueryTransient
    @JSONField(serialize = false, deserialize = false)
    default List<String> getRoleNameByCacheIds(final List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return Collections.emptyList();
        final RoleRepository roleRepository = getAppContext().getBean(RoleRepository.class);
        return ids.stream()
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(TabRole::getName)
                .collect(Collectors.toList());
    }
}