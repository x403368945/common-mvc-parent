package <%=pkg%>.code.<%=javaname%>.service;

import <%=pkg%>.code.<%=javaname%>.dao.jpa.<%=JavaName%>Repository;
import <%=pkg%>.code.<%=javaname%>.entity.<%=TabName%>;
import com.querydsl.core.QueryResults;
import com.support.aop.annotations.ServiceAspect;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.exception.DeleteRowsException;
import com.support.mvc.exception.UpdateRowsException;
import com.support.mvc.service.str.IOpenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

//import static <%=pkg%>.config.init.BeanInitializer.Beans.cacheManager; // 若使用缓存需要解开代码

/**
 * 服务接口实现类：<%=comment%>
 *
 * @author 谢长春 on <%=date%>
 */
@Slf4j
@Service
@ServiceAspect
@RequiredArgsConstructor
public class <%=JavaName%>Service implements IOpenService<<%=TabName%>>
//	, I<%=TabName%>Cache
 {
    private final <%=JavaName%>Repository repository;
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
//     public void clearKeys(final Collection<<%=id%>> ids) {
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

/*
   // 注释掉的方法只有在需要的时候解开
    @Override
    public <%=TabName%> deleteById(final <%=id%> id) {
        return repository.deleteById(id);
    }
*/

    @Override
    public void markDeleteById(final <%=id%> id) {
        DeleteRowsException.asserts(repository.markDeleteById(id));
    }

    @Override
    public void markDeleteByIds(final List<<%=id%>> ids) {
        DeleteRowsException.asserts(repository.markDeleteByIds(ids), ids.size());
        //clearKeys(ids); // 若使用缓存需要解开代码
    }

/*  // 注释掉的方法只有在需要的时候解开
    @Override
    public void markDelete(final List<<%=TabName%>> list) {
        DeleteRowsException.asserts(repository.markDelete(list), list.size());
        //clearKeys(list.stream().map(<%=TabName%>::getStringId).collect(Collectors.toSet())); // 若使用缓存需要解开代码
    }
*/

    @Override
    public Optional<<%=TabName%>> findById(final <%=id%> id) {
        return repository.findById(id);
//         return Optional.ofNullable(repository.findCacheById(id)); // 若使用缓存需要解开代码
    }

/*
    // 非必要情况下不要开放列表查询方法，因为没有分页控制，容易内存溢出。大批量查询数据应该使用分页查询
    @Override
    public List<<%=TabName%>> findList(final <%=TabName%> condition) {
        return repository.findList(condition);
    }
*/

    @Override
    public QueryResults<<%=TabName%>> findPage(final <%=TabName%> condition, final Pager pager) {
        return repository.findPage(condition, Pager.rebuild(pager));
    }
}
