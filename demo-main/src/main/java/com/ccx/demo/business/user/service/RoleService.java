package com.ccx.demo.business.user.service;

import com.ccx.demo.business.user.dao.jpa.RoleRepository;
import com.ccx.demo.business.user.entity.Authority;
import com.ccx.demo.business.user.entity.QTabRole;
import com.ccx.demo.business.user.entity.TabRole;
import com.ccx.demo.enums.Radio;
import com.querydsl.core.QueryResults;
import com.support.aop.annotations.ServiceAspect;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.exception.DeleteRowsException;
import com.support.mvc.exception.UpdateRowsException;
import com.support.mvc.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 服务接口实现类：角色表
 *
 * @author 谢长春 on 2019-08-29
 */
@Slf4j
@Service
@ServiceAspect
public class RoleService implements IService<TabRole> {
    @Autowired
    private RoleRepository repository;
    @Autowired
    private AuthorityService authorityService;

    @Override
    public TabRole save(final TabRole obj, final Long userId) {
        final List<String> authorities = authorityService.expendFilterCheckedList(obj.getAuthorityTree())
                .stream()
                .map(Authority::getCode)
                .map(Enum::name)
                .collect(Collectors.toList());
        Assert.notEmpty(authorities, "权限指令树【authorityTree】配置无效，有效的权限指令代码不能为空");
        obj.setAuthorities(authorities);
        return repository.save(obj);
    }

    @Override
    public void update(final Long id, final Long userId, final TabRole obj) {
        final List<String> authorities = authorityService.expendFilterCheckedList(obj.getAuthorityTree())
                .stream()
                .map(Authority::getCode)
                .map(Enum::name)
                .collect(Collectors.toList());
        Assert.notEmpty(authorities, "权限指令树【authorityTree】配置无效，有效的权限指令代码不能为空");
        obj.setAuthorities(authorities);
        UpdateRowsException.asserts(repository.update(id, userId, obj));
    }

    @Override
    public void markDeleteByUid(final Long id, final String uid, final Long userId) {
        DeleteRowsException.asserts(repository.markDeleteByUid(id, uid, userId));
    }

    @Override
    public void markDelete(final List<TabRole> list, final Long userId) {
        DeleteRowsException.warn(repository.markDelete(list, userId), list.size());
    }

    @Override
    public Optional<TabRole> findByUid(final Long id, final String uid) {
        return repository.findByUid(id, uid);
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
    public @NotEmpty(message = "matchValidRoleIds:返回值不能为空集合") List<Long> matchValidRoleIds(
            @NotEmpty(message = "【ids】不能为空") final List<TabRole> roles) {
        return repository.findValidRoleIds(roles);
    }

    /**
     * 获取前端角色选项集合
     *
     * @return {@link List<TabRole>}
     */
    public List<TabRole> getOptions() {
        final QTabRole q = QTabRole.tabRole;
        return repository.findList(
                TabRole.builder().deleted(Radio.NO).build(),
                q.id,
                q.uid,
                q.name
        );
    }

}
