import {saveAuth} from '../service/save';
import {saveAllAuth} from '../service/saveAll';
import {updateAuth} from '../service/update';
import {deleteByIdAuthSpare} from '../service/deleteById';
import {deleteByUidAuthSpare} from '../service/deleteByUid';
import {markDeleteByIdAuthSpare} from "../service/markDeleteById";
import {markDeleteByUidAuth} from '../service/markDeleteByUid';
import {markDeleteByIdsAuthSpare} from '../service/markDeleteByIds';
import {markDeleteAuth} from '../service/markDelete';
import {findByIdAuthSpare} from '../service/findById';
import {findByUidAuth} from '../service/findByUid';
import {findPageAuth} from '../service/findPage';
import {findListAuthSpare} from '../service/findList';

/**
 *
 * @param table {Table}
 */
const Controller = (table) => {
  const {
    pkg,
    comment,
    date,
    names: {javaname, TabName, JavaName}
  } = table;
  return `package ${pkg}.code.${javaname}.service;

import ${pkg}.code.${javaname}.dao.jpa.${JavaName}Repository;
import ${pkg}.code.${javaname}.entity.${TabName};
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

import java.util.*;

//import static ${pkg}.config.init.BeanInitializer.Beans.cacheManager; // 若使用缓存需要解开代码

/**
 * 服务接口实现类：${comment} <
 *
 * @author 谢长春 on ${date}
 */
@Slf4j
@Service
@ServiceAspect
@RequiredArgsConstructor
public class ${JavaName}Service implements IService<${TabName}>
//      , I${TabName}Cache
{
    private final ${JavaName}Repository repository;
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
//      * @param ids {@link ${TabName}#getId()}
//      */
//     public void clearKeys(final Collection<${id}> ids) {
//         ids.stream().distinct().forEach(id -> getCacheManager().evict(id));
//     }

${saveAuth(table)}
${saveAllAuth(table)}
${updateAuth(table)}
${deleteByIdAuthSpare(table)}
${deleteByUidAuthSpare(table)}
${markDeleteByIdAuthSpare(table)}
${markDeleteByUidAuth(table)}
${markDeleteByIdsAuthSpare(table)}
${markDeleteAuth(table)}
${findByIdAuthSpare(table)}
${findByUidAuth(table)}
${findPageAuth(table)}
${findListAuthSpare(table)}
}
`;
};
export default Controller
