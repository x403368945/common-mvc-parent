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

//import static com.ccx.demo.config.init.BeanInitializer.Beans.cacheManager; // 若使用缓存需要解开代码

/**
 * 服务接口实现类：测试自定义 Convert 表
 *
 * @author 谢长春 on 2019-08-21
 */
@Slf4j
@Service
@ServiceAspect
@RequiredArgsConstructor
public class ConvertService implements IBaseService<TabConvert>
//      , ITabConvertCache
{
    private final ConvertRepository repository;
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
//      * @param ids {@link TabConvert#getId()}
//      */
//     public void clearKeys(final Collection<Long> ids) {
//         ids.stream().distinct().forEach(id -> getCacheManager().evict(id));
//     }


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

/*
   // 注释掉的方法只有在需要的时候解开
    @Override
    public TabConvert deleteById(final Long id, final Long userId) {
        return repository.deleteById(id, userId);
    }
*/
/*
    // 注释掉的方法只有在需要的时候解开
    @Override
    public TabConvert deleteByUid(final Long id, final String uid, final Long userId) {
        return repository.deleteByUid(id, uid, userId);
    }
*/
/*
    // 注释掉的方法只有在需要的时候解开
    @Override
    public void markDeleteById(final Long id, final Long userId) {
        DeleteRowsException.asserts(repository.markDeleteById(id, userId));
    }
*/

    @Override
    public void markDeleteByUid(final Long id, final String uid, final Long userId) {
        DeleteRowsException.asserts(repository.markDeleteByUid(id, uid, userId));
    }

/*
    // 注释掉的方法只有在需要的时候解开 <
    @Override
    public void markDeleteByIds(final Set<Long> ids, final Long userId) {
        DeleteRowsException.asserts(repository.markDeleteByIds(ids, userId), ids.size());
        //clearKeys(ids); // 若使用缓存需要解开代码
    }
*/

    @Override // <
    public void markDelete(final List<MarkDelete> list, final Long userId) {
        DeleteRowsException.asserts(repository.markDelete(list, userId), list.size());
        //clearKeys(list.stream().map(MarkDelete::getLongId).collect(Collectors.toSet())); // 若使用缓存需要解开代码
    }

/*
    @Override // <
    public Optional<TabConvert> findById(final Long id) {
        return repository.findById(id);
//         return Optional.ofNullable(repository.findCacheById(id)); // 若使用缓存需要解开代码
    }
*/

    @Override
    public Optional<TabConvert> findByUid(final Long id, final String uid) {
        return repository.findById(id).filter(row -> Objects.equals(row.getUid(), uid));
//         return Optional.ofNullable(repository.findCacheById(id)).filter(row -> Objects.equals(uid, row.getUid())); // 若使用缓存需要解开代码
    }


    @Override // <
    public QueryResults<TabConvert> findPage(final TabConvert condition, final Pager pager) {
        return repository.findPage(condition, Pager.rebuild(pager));
    }

/*
    // 非必要情况下不要开放列表查询方法，因为没有分页控制，容易内存溢出。大批量查询数据应该使用分页查询 <
    @Override
    public List<TabConvert> findList(final TabConvert condition) {
        return repository.findList(condition);
    }
*/
}
