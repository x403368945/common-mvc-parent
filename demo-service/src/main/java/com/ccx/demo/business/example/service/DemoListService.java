package com.ccx.demo.business.example.service;

import com.ccx.demo.business.example.dao.jpa.DemoListRepository;
import com.ccx.demo.business.example.entity.TabDemoList;
import com.ccx.demo.business.example.vo.TabDemoListVO;
import com.querydsl.core.QueryResults;
import com.support.aop.annotations.ServiceAspect;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.exception.DeleteRowsException;
import com.support.mvc.exception.UpdateRowsException;
import com.support.mvc.service.IService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 服务接口实现类：
 *
 * @author 谢长春 on 2018-12-17
 */
@Slf4j
@Service
@ServiceAspect
@RequiredArgsConstructor
public class DemoListService implements IService<TabDemoList> {
    private final DemoListRepository repository;

    @Override
    public TabDemoList save(final TabDemoList obj, final Long userId) {
        return repository.save(obj);
    }

    @Override
    public List<TabDemoList> saveAll(final List<TabDemoList> list, final Long userId) {
        return repository.saveAll(list);
    }

    @Override
    public void update(final Long id, final Long userId, final TabDemoList obj) {
        UpdateRowsException.asserts(repository.update(id, userId, obj));
    }

    @Override
    public TabDemoList deleteById(final Long id, final Long userId) {
        return repository.deleteById(id, userId);
    }

    @Override
    public TabDemoList deleteByUid(final Long id, final String uid, final Long userId) {
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
        DeleteRowsException.warn(repository.markDeleteByIds(ids, userId), ids.size());
    }

    @Override
    public void markDelete(final List<MarkDelete> list, final Long userId) {
        DeleteRowsException.warn(repository.markDelete(list, userId), list.size());
    }

    @Override
    public Optional<TabDemoList> findById(final Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<TabDemoList> findByUid(final Long id, final String uid) {
        return repository.findById(id).filter(row -> Objects.equals(row.getUid(), uid));
    }

    @Override
    public List<TabDemoList> findList(final TabDemoList condition) {
        return repository.findList(condition);
    }

    @Override
    public QueryResults<TabDemoList> findPage(final TabDemoList condition, final Pager pager) {
        return repository.findPage(condition, Pager.rebuild(pager));
    }

    public List<TabDemoListVO> findListVO(final TabDemoList condition) {
        return repository.findListProjection(condition, TabDemoListVO.class);
    }

    public QueryResults<TabDemoListVO> findPageVO(final TabDemoList condition, final Pager pager) {
        return repository.findPageProjection(condition, Pager.rebuild(pager), TabDemoListVO.class);
    }

    public void findListTest() {
        repository.findListTest();
    }
}
