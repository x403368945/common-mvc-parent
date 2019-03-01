package com.boot.demo.business.{javaname}.service;

import com.boot.demo.business.{javaname}.dao.jpa.{JavaName}Repository;
import com.boot.demo.business.{javaname}.entity.{TabName};
import com.querydsl.core.QueryResults;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 服务接口实现类：{comment}
 *
 * @author 谢长春 on {date}
 */
@Slf4j
@Service
public class {JavaName}Service implements ISearchService<{TabName}> {
    @Autowired
    private {JavaName}Repository repository;

    @Override
    public Optional<{TabName}> findById(final {ID} id) {
        return repository.findById(id);
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
