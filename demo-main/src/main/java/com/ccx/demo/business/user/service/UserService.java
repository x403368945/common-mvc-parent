package com.ccx.demo.business.user.service;

import com.ccx.demo.business.user.bordcast.IUserEvent;
import com.ccx.demo.business.user.dao.jpa.UserRepository;
import com.ccx.demo.business.user.entity.TabUser;
import com.ccx.demo.enums.Role;
import com.google.common.eventbus.EventBus;
import com.querydsl.core.QueryResults;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.exception.DeleteRowsException;
import com.support.mvc.exception.UpdateRowsException;
import com.support.mvc.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

/**
 * 服务接口实现类：用户
 *
 * @author 谢长春 on 2017/10/12.
 */
@Slf4j
@Service
public class UserService implements IService<TabUser> {

    @Autowired
    private UserRepository repository;
    @Autowired
    private EventBus eventBus;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
//    @Autowired
//    private UserCache userCache;
//    @Autowired
//    private PhoneCode phoneCode;

    @Override
    public TabUser save(final TabUser obj, final Long userId) {
        final List<Long> validRoleIds = roleService.matchValidRoleIds(obj.getRoleList());
        obj.setRoles(validRoleIds);
        obj.setPassword(passwordEncoder.encode(obj.getPassword()));
        final TabUser user = repository.save(obj);
        { // 发送广播
            eventBus.post(IUserEvent.UserNew.of(user));
        }
        return user;
    }

    @Override
    public void update(final Long id, final Long userId, final TabUser obj) {
        final List<Long> validRoleIds = roleService.matchValidRoleIds(obj.getRoleList());
        obj.setRoles(validRoleIds);

        UpdateRowsException.asserts(repository.update(id, userId, obj));

        clearCache(id);
    }

    @Override
    public Optional<TabUser> findByUid(final Long id, final String uid) {
        return repository.findByUid(id, uid);
    }

    @Override
    public void markDeleteById(final Long id, final Long userId) {
        UpdateRowsException.asserts(repository.markDeleteById(id, userId));
        clearCache(id);
    }

    @Override
    public void markDeleteByIds(final List<Long> ids, final Long userId) {
        DeleteRowsException.warn(repository.markDeleteByIds(ids, userId), ids.size());
        ids.forEach(this::clearCache);
    }

    @Override
    public QueryResults<TabUser> findPage(final TabUser condition, final Pager pager) {
        return repository.findPage(condition, Pager.rebuild(pager));
    }

    /**
     * 按用户名查找用户信息，（查询范围：username|phone|password）
     *
     * @param username String 用户名
     * @return Optional<TabUser>
     */
    public Optional<TabUser> findUser(final String username) {
        return repository.findUser(username);
    }
//    /**
//     * 启用账户
//     *
//     * @param id     Long 用户ID
//     * @param userId Long 修改者ID
//     */
//    @Transactional(rollbackFor = Exception.class)
//    public void enable(final Long id, final Long userId) {
//        UpdateRowsException.asserts(repository.enable(id, userId));
//    }

    /**
     * 更新数据；使用 aop 拦截，通过 @ServiceAspect 注解设置用户信息
     *
     * @param id       {@link Long} 数据ID
     * @param userId   {@link Long} 操作用户ID
     * @param nickname String 昵称
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateNickname(@NotNull(message = "【id】不能为null") @Positive(message = "【id】必须大于0") final Long id,
                               @NotBlank(message = "【nickname】不能为null") final String nickname,
                               @NotNull(message = "【userId】不能为null") @Positive(message = "【userId】必须大于0") final Long userId) {
        UpdateRowsException.asserts(repository.updateNickname(id, nickname, userId));
        clearCache(id);
        { // 发送广播
            eventBus.post(IUserEvent.NicknameUpdate.of(TabUser.builder().id(id).nickname(nickname).build()));
        }
    }

    /**
     * 修改密码、重置密码
     *
     * @param id       String 用户ID
     * @param password String 新密码
     * @param userId   Long 修改者ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(
            @NotNull(message = "【id】不能为null") @Positive(message = "【id】必须大于0") final Long id,
            @NotBlank(message = "【password】不能为null") final String password,
            @NotNull(message = "【userId】不能为null") @Positive(message = "【userId】必须大于0") final Long userId) {
        UpdateRowsException.asserts(repository.updatePassword(id, passwordEncoder.encode(password), userId));
        clearCache(id);
    }

    /**
     * 清除指定用户的缓存
     *
     * @param id Long 用户ID
     */
    private void clearCache(final Long id) {
        repository.findById(id).ifPresent(obj -> { // 清除登录查询缓存
            repository.clearLoginCache(obj.getUsername());
            repository.clearLoginCache(obj.getPhone());
            repository.clearLoginCache(obj.getEmail());
        });
    }

}