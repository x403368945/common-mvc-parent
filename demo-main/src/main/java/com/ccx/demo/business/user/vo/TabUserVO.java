package com.ccx.demo.business.user.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.ccx.demo.business.user.entity.TabUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户实体
 *
 * @author 谢长春
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JSONType(orders = {"id", "uid", "subdomain", "username", "nickname", "phone", "email", "role", "registerSource", "deleted"})
public class TabUserVO extends TabUser {
    @SneakyThrows
    public static TabUserVO ofTabUser(final TabUser entity) {
        final TabUserVO vo = new TabUserVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    /**
     * 权限指令集合
     */
    private List<String> authorityList;

    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    public List<String> getAuthorityList() {
        if (CollectionUtils.isEmpty(authorityList)) {
            authorityList = Objects.requireNonNull(getRoles(), "当前登录账户未配置权限").stream()
                    .flatMap(id -> getRoleAuthoritiesCacheById(id).stream())
                    .distinct()
                    .collect(Collectors.toList());
        }
        return authorityList;
    }
}