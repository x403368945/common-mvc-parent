package <%=pkg%>.code.<%=javaname%>.service;

import <%=pkg%>.code.<%=javaname%>.dao.jpa.<%=JavaName%>Repository;
import <%=pkg%>.code.<%=javaname%>.entity.<%=TabName%>;
import com.querydsl.core.QueryResults;
import com.support.aop.annotations.ServiceAspect;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.exception.DeleteRowsException;
import com.support.mvc.exception.UpdateRowsException;
import com.support.mvc.service.str.ISimpleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

//import static <%=pkg%>.config.init.BeanInitializer.Beans.cacheManager; // 若使用缓存需要解开代码

/**
 * 服务接口实现类：<%=comment%>
 *
 * @author 谢长春 on <%=date%>
 */
@Slf4j
@Service
@ServiceAspect
public class <%=JavaName%>Service implements ISimpleService<<%=TabName%>>, /* I<%=TabName%>Cache */ {
    @Autowired
    private <%=JavaName%>Repository repository;
//     /** // 若使用缓存需要解开代码
//      * 获取当前缓存管理器，用于代码控制缓存
//      *
//      * @return {@link Cache}
//      */
//     public Cache getCacheManager() {
//         return Objects.requireNonNull(cacheManager.<CacheManager>get().getCache(CACHE_ROW_BY_ID), "未获取到缓存管理对象:".concat(CACHE_ROW_BY_ID));
//     }
//
//     /** // 若使用缓存需要解开代码
//      * 清除多个 key 对应的缓存
//      *
//      * @param ids {@link <%=TabName%>#getId()}
//      */
//     public void clearKeys(Collection<<%=id%>> ids) {
//         ids.stream().distinct().forEach(id -> getCacheManager().evict(id));
//     }

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
    public <%=TabName%> deleteById(final <%=id%> id) {
        return repository.deleteById(id);
    }

    @Override
    public void markDeleteById(final <%=id%> id) {
        DeleteRowsException.asserts(repository.markDeleteById(id));
    }

    @Override
    public void markDeleteByIds(final List<<%=id%>> ids) {
        DeleteRowsException.warn(repository.markDeleteByIds(ids), ids.size());
        //clearKeys(ids); // 若使用缓存需要解开代码
    }

    @Override
    public void markDelete(final List<<%=TabName%>> list) {
        DeleteRowsException.warn(repository.markDelete(list), list.size());
        //clearKeys(list.stream().map(<%=TabName%>::getId).collect(Collectors.toSet())); // 若使用缓存需要解开代码
    }

    @Override
    public Optional<<%=TabName%>> findById(final <%=id%> id) {
        return repository.findById(id);
//         return Optional.ofNullable(repository.findCacheById(id)); // 若使用缓存需要解开代码
    }

//    @Override
//    public Optional<<%=TabName%>> findByUid(final <%=id%> id, final String uid) {
//        return repository.findById(id).filter(row -> Objects.equals(row.getUid(), uid));
//         return Optional.ofNullable(repository.findCacheById(id)).filter(row -> Objects.equals(uid, row.getUid())); // 若使用缓存需要解开代码
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
