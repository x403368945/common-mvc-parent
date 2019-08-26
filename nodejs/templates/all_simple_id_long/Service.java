package <%=pkg%>.code.<%=javaname%>.service;

import <%=pkg%>.code.<%=javaname%>.dao.jpa.<%=JavaName%>Repository;
import <%=pkg%>.code.<%=javaname%>.entity.<%=TabName%>;
import com.querydsl.core.QueryResults;
import com.support.aop.annotations.ServiceAspect;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.exception.DeleteRowsException;
import com.support.mvc.exception.UpdateRowsException;
import com.support.mvc.service.ISimpleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 服务接口实现类：<%=comment%>
 *
 * @author 谢长春 on <%=date%>
 */
@Slf4j
@Service
@ServiceAspect
public class <%=JavaName%>Service implements ISimpleService<<%=TabName%>> {
    @Autowired
    private <%=JavaName%>Repository repository;

    @Override
    public <%=TabName%> save(final <%=TabName%> obj) {
        return repository.save(obj);
    }

    @Override
    public List<<%=TabName%>> saveAll(final List<<%=TabName%>> list) {
        return repository.saveAll(list);
    }

    @Override
    public void update(final <%=id%> id, final <%=TabName%> obj) {
        UpdateRowsException.asserts(repository.update(id, obj));
    }

    @Override
    public void markDeleteById(final <%=id%> id) {
        DeleteRowsException.asserts(repository.markDeleteById(id));
    }

    @Override
    public void markDeleteByIds(final List<<%=id%>> ids) {
        DeleteRowsException.warn(repository.markDeleteByIds(ids), ids.size());
    }

    @Override
    public Optional<<%=TabName%>> findById(final <%=id%> id) {
        return repository.findById(id);
    }

    @Override
    public QueryResults<<%=TabName%>> findPage(final <%=TabName%> condition, final Pager pager) {
        return repository.findPage(condition, Pager.rebuild(pager));
    }
}
