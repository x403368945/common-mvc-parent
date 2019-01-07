package com.demo.business.user.service;

import com.demo.business.user.dao.jpa.UserLoginRepository;
import com.demo.business.user.entity.TabUserLogin;
import com.mvc.entity.base.Pager;
import com.mvc.service.IService;
import com.querydsl.core.QueryResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 服务接口实现类：用户登录记录
 *
 * @author 谢长春 on 2017/10/12.
 */
@Slf4j
@Service
public class UserLoginService implements IService<TabUserLogin> {

    @Autowired
    private UserLoginRepository repository;

    @Override
    public TabUserLogin save(final TabUserLogin obj, final Long userId) {
        return repository.save(obj);
    }

    @Override
    public QueryResults<TabUserLogin> findPage(final TabUserLogin condition, final Pager pager) {
        return repository.findPage(condition, Pager.rebuild(pager));
    }
}