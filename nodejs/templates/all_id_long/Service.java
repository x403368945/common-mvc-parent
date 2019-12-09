package <%=pkg%>.code.<%=javaname%>.service;

import <%=pkg%>.code.<%=javaname%>.dao.jpa.<%=JavaName%>Repository;
import <%=pkg%>.code.<%=javaname%>.entity.<%=TabName%>;
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
import java.util.Objects;
import java.util.Optional;

import static com.ccx.demo.config.init.BeanInitializer.Beans.cacheManager;

/**
 * 服务接口实现类：<%=comment%>
 *
 * @author 谢长春 on <%=date%>
 */
@Slf4j
@Service
@ServiceAspect
public class <%=JavaName%>Service implements IService<<%=TabName%>>, I<%=TabName%>Cache {
    @Autowired
    private <%=JavaName%>Repository repository;
    /**
     * 获取当前缓存管理器，用于代码控制缓存
     *
     * @return {@link Cache}
     */
    public Cache getCacheManager() {
        return Objects.requireNonNull(cacheManager.<CacheManager>get().getCache(CACHE_ROW_BY_ID), "未获取到缓存管理对象:".concat(CACHE_ROW_BY_ID));
    }

    /**
     * 清除多个 key 对应的缓存
     *
     * @param ids {@link <%=TabName%>#getId()}
     */
    public void clearKeys(Collection<Long> ids) {
        ids.stream().distinct().forEach(id -> getCacheManager().evict(id));
    }

    @Override
    public <%=TabName%> save(final <%=TabName%> obj, final Long userId) {
        return repository.save(obj);
    }

    @Override
    public List<<%=TabName%>> saveAll(final List<<%=TabName%>> list, final Long userId) {
        return repository.saveAll(list);
    }

    @Override
    public void update(final <%=id%> id, final Long userId, final <%=TabName%> obj) {
        UpdateRowsException.asserts(repository.update(id, userId, obj));
    }

    @Override
    public <%=TabName%> deleteById(final <%=id%> id, final Long userId) {
        return repository.deleteById(id, userId);
    }

//    @Override
//    public <%=TabName%> deleteByUid(final <%=id%> id, final String uid, final Long userId) {
//        return repository.deleteByUid(id, uid, userId);
//    }

    @Override
    public void markDeleteById(final <%=id%> id, final Long userId) {
        DeleteRowsException.asserts(repository.markDeleteById(id, userId));
    }

//    @Override
//    public void markDeleteByUid(final <%=id%> id, final String uid, final Long userId) {
//        DeleteRowsException.asserts(repository.markDeleteByUid(id, uid, userId));
//    }

    @Override
    public void markDeleteByIds(final List<<%=id%>> ids, final Long userId) {
        DeleteRowsException.warn(repository.markDeleteByIds(ids, userId), ids.size());
    }

    @Override
    public void markDelete(final List<<%=TabName%>> list, final Long userId) {
        DeleteRowsException.warn(repository.markDelete(list, userId), list.size());
        clearKeys(list.stream().map(TabRole::getId).collect(Collectors.toSet()));
    }

    @Override
    public Optional<<%=TabName%>> findById(final <%=id%> id) {
        return repository.findById(id);
    }

//    @Override
//    public Optional<<%=TabName%>> findByUid(final <%=id%> id, final String uid) {
//        return repository.findById(id).filter(row -> Objects.equals(row.getUid(), uid));
//    }

    @Override
    public List<<%=TabName%>> findList(final <%=TabName%> condition) {
        return repository.findList(condition);
    }

    @Override
    public QueryResults<<%=TabName%>> findPage(final <%=TabName%> condition, final Pager pager) {
        return repository.findPage(condition, Pager.rebuild(pager));
    }
}
