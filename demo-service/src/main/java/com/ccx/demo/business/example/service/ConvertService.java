package com.ccx.demo.business.example.service;

import com.ccx.demo.business.example.dao.jpa.ConvertRepository;
import com.ccx.demo.business.example.entity.TabConvert;
import com.querydsl.core.QueryResults;
import com.support.aop.annotations.ServiceAspect;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.exception.DeleteRowsException;
import com.support.mvc.exception.UpdateRowsException;
import com.support.mvc.service.IBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 服务接口实现类：测试自定义 Convert 表
 *
 * @author 谢长春 on 2019-08-21
 */
@Slf4j
@Service
@ServiceAspect
@RequiredArgsConstructor
public class ConvertService implements IBaseService<TabConvert> {
    private final ConvertRepository repository;

    @Override
    public TabConvert save(final TabConvert obj, final Long userId) {
        return repository.save(obj);
    }

    @Override
    public List<TabConvert> saveAll(final List<TabConvert> list, final Long userId) {
        return repository.saveAll(list);
    }

    @Override
    public void update(final Long id, final Long userId, final TabConvert obj) {
        UpdateRowsException.asserts(repository.update(id, userId, obj));
    }

    @Override
    public TabConvert deleteById(final Long id, final Long userId) {
        return repository.deleteById(id, userId);
    }

    @Override
    public TabConvert deleteByUid(final Long id, final String uid, final Long userId) {
        return repository.deleteByUid(id, uid, userId);
    }

    @Override
    public void markDeleteById(final Long id, final Long userId) {
        DeleteRowsException.asserts(repository.markDeleteById(id, userId));
    }

    @Override
    public void markDeleteByUid(final Long id, final String uid, final Long userId) {
        DeleteRowsException.asserts(repository.markDeleteByUid(id, uid, userId));
    }

    @Override
    public void markDeleteByIds(final List<Long> ids, final Long userId) {
        DeleteRowsException.asserts(repository.markDeleteByIds(ids, userId), ids.size());
    }

    @Override
    public void markDelete(final List<MarkDelete> list, final Long userId) {
        DeleteRowsException.asserts(repository.markDelete(list, userId), list.size());
    }

    @Override
    public Optional<TabConvert> findById(final Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<TabConvert> findByUid(final Long id, final String uid) {
        return repository.findById(id).filter(row -> Objects.equals(row.getUid(), uid));
    }

    @Override
    public List<TabConvert> findList(final TabConvert condition) {
        return repository.findList(condition);
    }

    @Override
    public QueryResults<TabConvert> findPage(final TabConvert condition, final Pager pager) {
        return repository.findPage(condition, Pager.rebuild(pager));
    }
}
