package com.ccx.demo.business.user.cache;

import com.alibaba.fastjson.annotation.JSONField;
import com.ccx.demo.business.user.dao.jpa.RoleRepository;
import com.ccx.demo.business.user.entity.TabRole;
import com.ccx.demo.enums.Bool;
import com.querydsl.core.annotations.QueryTransient;
import com.support.mvc.entity.ICache;
import org.springframework.util.CollectionUtils;

import java.beans.Transient;
import java.util.*;
import java.util.stream.Collectors;

import static com.ccx.demo.config.init.BeanInitializer.getAppContext;

/**
 * <pre>
 * 缓存角色权限
 *
 * @author 谢长春 2019/8/29
 */
public interface ITabRoleCache extends ICache {
    String CACHE_ROW_BY_ID = "ITabRoleCache";

    /**
     * 按 ID 获取数据缓存行
     *
     * @param id {@link TabRole#getId()}
     * @return {@link Optional<TabRole>}
     */
    @JSONField(serialize = false, deserialize = false)
    default Optional<TabRole> getTabRoleCacheById(final Long id) {
        return Optional.ofNullable(id)
                .filter(value -> value > 0)
                .map(value ->getAppContext().getBean(RoleRepository.class).findCacheById(value));
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
    default List<String> getRoleAuthoritiesCacheById(final Long id) {
        return getTabRoleCacheById(id)
                .filter(obj -> Objects.equals(obj.getDeleted(), Bool.NO))
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
    default String getRoleNameCacheById(final Long id) {
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
    default List<String> getRoleNameCacheByIds(final Collection<Long> ids) {
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