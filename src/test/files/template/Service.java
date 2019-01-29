package com.boot.demo.business.{javaname}.service;

import com.boot.demo.business.{javaname}.dao.jpa.{JavaName}Repository;
import com.boot.demo.business.{javaname}.entity.{TabName};
import com.querydsl.core.QueryResults;
import com.support.aop.annotations.ServiceAspect;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.exception.DeleteRowsException;
import com.support.mvc.exception.UpdateRowsException;
import com.support.mvc.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 服务接口实现类：
 *
 * @author 谢长春 on {date}
 */
@Slf4j
@Service
@ServiceAspect
public class {JavaName}Service implements IService<{TabName}> {
    @Autowired
    private {JavaName}Repository repository;

    @Override
    public {TabName} save(final {TabName} obj, final Long userId) {
        return repository.save(obj);
    }

    @Override
    public List<{TabName}> saveAll(final List<{TabName}> list, final Long userId) {
        return repository.saveAll(list);
    }

    @Override
    public void update(final {ID} id, final Long userId, final {TabName} obj) {
        UpdateRowsException.asserts(repository.update(id, userId, obj));
    }

    @Override
    public {TabName} deleteById(final {ID} id, final Long userId) {
        return repository.deleteById(id, userId);
    }

    @Override
    public {TabName} deleteByUid(final {ID} id, final String uid, final Long userId) {
        return repository.deleteByUid(id, uid, userId);
    }

    @Override
    public void markDeleteById(final {ID} id, final Long userId) {
        DeleteRowsException.asserts(repository.markDeleteById(id, userId));
    }

    @Override
    public void markDeleteByUid(final {ID} id, final String uid, final Long userId) {
        DeleteRowsException.asserts(repository.markDeleteByUid(id, uid, userId));
    }

    @Override
    public void markDeleteByIds(final List<{ID}> ids, final Long userId) {
        DeleteRowsException.warn(repository.markDeleteByIds(ids, userId), ids.size());
    }

    @Override
    public void markDelete(final List<{TabName}> list, final Long userId) {
        DeleteRowsException.warn(repository.markDelete(list, userId), list.size());
    }

    @Override
    public Optional<{TabName}> findById(final {ID} id) {
        return repository.findById(id);
    }

    @Override
    public Optional<{TabName}> findByUid(final {ID} id, final String uid) {
        return repository.findById(id, uid);
    }

    @Override
    public List<{TabName}> findList(final {TabName} condition) {
        return repository.findList(condition);
    }

    @Override
    public QueryResults<{TabName}> findPage(final {TabName} condition, final Pager pager) {
        return repository.findPage(condition, Pager.rebuild(pager));
    }
}
