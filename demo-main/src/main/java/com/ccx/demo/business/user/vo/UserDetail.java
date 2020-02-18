package com.ccx.demo.business.user.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.ccx.demo.business.user.cache.ITabRoleCache;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.enums.Bool;
import com.querydsl.core.annotations.QueryTransient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Spring Security 鉴权信息
 *
 * @author 谢长春 on 2018/3/15.
 */
public class UserDetail implements UserDetails, ITabRoleCache {

    public TabUser loadUserDetail() {
        return null;
    }

    public TabUserVO toTabUserVO() {
        return TabUserVO.ofTabUser((TabUser) this);
    }

    @QueryTransient
    @JSONField(serialize = false, deserialize = false)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singletonList(loadUserDetail().getRole().toAuthority());
        final List<String> authorityList = Stream.of(loadUserDetail().getRoles())
                .flatMap(id -> getRoleAuthoritiesCacheById(id).stream())
                .distinct()
                .collect(Collectors.toList());
//        setAuthorityList(authorityList);
        return authorityList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @QueryTransient
    @JSONField(serialize = false, deserialize = false)
    @Override
    public String getPassword() {
        return loadUserDetail().getPassword();
    }

    @QueryTransient
    @JSONField(serialize = false, deserialize = false)
    @Override
    public String getUsername() {
        return loadUserDetail().getUsername();
    }

    @QueryTransient
    @JSONField(serialize = false, deserialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
//        return Objects.equals(Bool.NO, user.getExpired());
    }

    @QueryTransient
    @JSONField(serialize = false, deserialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @QueryTransient
    @JSONField(serialize = false, deserialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @QueryTransient
    @JSONField(serialize = false, deserialize = false)
    @Override
    public boolean isEnabled() {
        return Objects.equals(Bool.NO, loadUserDetail().getDeleted());
    }
}
