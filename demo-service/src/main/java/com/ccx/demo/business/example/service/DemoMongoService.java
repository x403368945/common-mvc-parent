package com.ccx.demo.business.example.service;

import com.ccx.demo.business.example.dao.mongo.DemoMongoRepository;
import com.ccx.demo.business.example.entity.DemoMongo;
import com.ccx.demo.enums.Radio;
import com.querydsl.core.QueryResults;
import com.support.aop.annotations.MongoServiceAspect;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.exception.DeleteRowsException;
import com.support.mvc.exception.UpdateRowsException;
import com.support.mvc.service.str.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 服务接口实现类：
 *
 *
 * @author 谢长春 on 2019-01-03
 */
@Slf4j
@Service
@MongoServiceAspect(deleted = Radio.class)
public class DemoMongoService implements IService<DemoMongo> {
    @Autowired
    private DemoMongoRepository repository;

    @Override
    public DemoMongo save(final DemoMongo obj, final Long userId) {
        return repository.save(obj);
    }

    @Override
    public List<DemoMongo> saveAll(final List<DemoMongo> list, final Long userId) {
        return repository.saveAll(list);
    }

    @Override
    public void update(final String id, final Long userId, final DemoMongo obj) {
        UpdateRowsException.asserts(repository.update(id, userId, obj));
    }

    @Override
    public DemoMongo deleteById(final String id, final Long userId) {
        return repository.deleteById(id, userId);
    }

    @Override
    public void markDeleteById(final String id, final Long userId) {
        DeleteRowsException.asserts(repository.markDeleteById(id, userId));
    }

    @Override
    public void markDeleteByIds(final List<String> ids, final Long userId) {
        DeleteRowsException.warn(repository.markDeleteByIds(ids, userId), ids.size());
    }

    @Override
    public Optional<DemoMongo> findById(final String id) {
        return repository.findById(id);
    }

    @Override
    public List<DemoMongo> findList(final DemoMongo condition) {
        return repository.findList(condition);
    }

    @Override
    public QueryResults<DemoMongo> findPage(final DemoMongo condition, final Pager pager) {
        return repository.findPage(condition, Pager.rebuild(pager));
    }
}
