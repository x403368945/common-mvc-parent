import DataType from './core/DataType';

/**
 *
 * @param table {Table}
 */
export const Entity = async (table) => {
  const JpaEntity = (await import('./entity/JpaEntity'));
  return JpaEntity.default(table)
};

/**
 *
 * @param table {Table}
 */
export const Http = async (table) => {
  const {comment, date} = table;
  return `### ********************************************************************************************************************
### ${comment}
### @author 谢长春 on ${date}
### ********************************************************************************************************************
${(await import('./http/page')).pageAuth(table)}
${(await import('./http/search')).searchAuthSpare(table)}
${(await import('./http/findById')).findByIdAuthSpare(table)}
${(await import('./http/findByUid')).findByUidAuth(table)}
${(await import('./http/save')).saveAuth(table)}
${(await import('./http/update')).updateAuth(table)}
${(await import('./http/deleteById')).deleteByIdAuthSpare(table)}
${(await import('./http/deleteByUid')).deleteByUidAuthSpare(table)}
${(await import('./http/markDeleteByUid')).markDeleteByUidAuth(table)}
${(await import('./http/markDeleteByIds')).markDeleteByIdsAuthSpare(table)}
${(await import('./http/markDelete')).markDeleteAuth(table)}
`
};

/**
 *
 * @param table {Table}
 */
export const Controller = async (table) => {
  const {
    pkg,
    idType,
    comment,
    date,
    names: {javaname, TabName, JavaName, java_name}
  } = table;
  return `package ${pkg}.code.${javaname}.web;

import com.alibaba.fastjson.JSON;
import ${pkg}.code.${javaname}.entity.${TabName};
import ${pkg}.business.user.entity.TabUser;
import ${pkg}.code.${javaname}.service.${JavaName}Service;
import ${pkg}.business.user.web.IAuthController;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

/**
 * 请求操作响应：${comment}
 *
 * @author 谢长春 on ${date}
 */
@Api(tags = "${comment}")
//@ApiSort(1) // 控制接口排序 <
@RequestMapping("/1/${java_name}")
@Controller
@Slf4j
@RequiredArgsConstructor
public class ${JavaName}Controller implements IAuthController<${idType}, ${TabName}> {

    private final ${JavaName}Service service;

${(await import('./controller/save')).saveAuth(table)}
${(await import('./controller/update')).updateAuth(table)}
${(await import('./controller/deleteById')).deleteByIdAuthSpare(table)}
${(await import('./controller/deleteByUid')).deleteByUidAuthSpare(table)}
${(await import('./controller/markDeleteByUid')).markDeleteByUidAuth(table)}
${(await import('./controller/markDeleteByIds')).markDeleteByIdsAuthSpare(table)}
${(await import('./controller/markDelete')).markDeleteAuth(table)}
${(await import('./controller/findById')).findByIdAuthSpare(table)}
${(await import('./controller/findByUid')).findByUidAuth(table)}
${(await import('./controller/page')).pageAuth(table)}
${(await import('./controller/search')).searchAuthSpare(table)}
}
`
};

/**
 *
 * @param table {Table}
 */
export const Service = async (table) => {
  const {
    pkg,
    comment,
    idType,
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
import com.support.mvc.service${idType === DataType.VARCHAR.java ? '.str' : ''}.IBaseService;
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
public class ${JavaName}Service implements IBaseService<${TabName}>
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
//     public void clearKeys(final Collection<${idType}> ids) {
//         ids.stream().distinct().forEach(id -> getCacheManager().evict(id));
//     }

${(await import('./service/save')).saveAuth(table)}
${(await import('./service/saveAll')).saveAllAuth(table)}
${(await import('./service/update')).updateAuth(table)}
${(await import('./service/deleteById')).deleteByIdAuthSpare(table)}
${(await import('./service/deleteByUid')).deleteByUidAuthSpare(table)}
${(await import('./service/markDeleteById')).markDeleteByIdAuthSpare(table)}
${(await import('./service/markDeleteByUid')).markDeleteByUidAuth(table)}
${(await import('./service/markDeleteByIds')).markDeleteByIdsAuthSpare(table)}
${(await import('./service/markDelete')).markDeleteAuth(table)}
${(await import('./service/findById')).findByIdAuthSpare(table)}
${(await import('./service/findByUid')).findByUidAuth(table)}
${(await import('./service/findPage')).findPageAuth(table)}
${(await import('./service/findList')).findListAuthSpare(table)}
}
`;
};

/**
 *
 * @param table {Table}
 */
export const Repository = async (table) => {
  const {
    pkg,
    comment,
    idType,
    date,
    names: {javaname, TabName, JavaName, tabName}
  } = table;
  return `package ${pkg}.code.${javaname}.dao.jpa;

import ${pkg}.code.${javaname}.entity.${TabName};
import ${pkg}.code.${javaname}.entity.Q${TabName};
import ${pkg}.enums.Bool;
import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.support.mvc.dao.IRepository;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.*;

import static ${pkg}.config.init.BeanInitializer.Beans.jpaQueryFactory;
import static ${pkg}.config.init.BeanInitializer.getAppContext;

/**
 * 数据操作：${comment} <
 *
 * @author 谢长春 on ${date}
 */
public interface ${JavaName}Repository extends
        JpaRepository<${TabName}, ${idType}>,
        IRepository<${TabName}, ${idType}> {
    // 每个 DAO 层顶部只能有一个查询实体,且必须以 q 命名,表示当前操作的数据库表. 当 q 作为主表的连接查询方法也必须写在这个类
    Q${TabName} q = Q${TabName}.${tabName};

    /**
     * 如果该表有缓存时请使用缓存，将这段代码注释，然后组合缓存接口。
     * 组合模式：定义表数据无缓存时，优化连表查询方法.
     * 实体类只需要组合该接口就可以获得按 id 查询方法.
     * 这种实现方式可以分解连表查询，减轻数据库压力，对分页查询有优化，让单表查询方法复用范围更广
     * 使用参考：
     * <pre>
     * public class TabEntity implements I${JavaName}Repository{
     *     public ${idType} foreignKey;
     *     public Set<${idType}> foreignKeys;
     *
     *     public ${TabName} getForeign(){
     *         // 连表查询单条记录
     *         return get${TabName}ById(foreignKey).orElse(null);
     *     }
     *     public List<${TabName}> getForeigns(){
     *         // 连表查询多条记录
     *         return get${TabName}ByIds(foreignKeys);
     *     }
     * }
     * </pre>
     */
    interface I${JavaName}Repository {
        /**
         * 按 ID 获取数据行，用于表数据无缓存时，优化连表查询
         *
         * @param id {@link ${TabName}#getId()}
         * @return {@link Optional<${TabName}>}
         */
        @JSONField(serialize = false, deserialize = false)
        default Optional<${TabName}> get${TabName}ById(final ${idType} id) {
            return getAppContext().getBean(${JavaName}Repository.class).findById(id);
        }

        /**
         * 按 ID 获取数据行，用于表数据无缓存时，优化连表查询
         *
         * @param ids {@link ${TabName}#getId()}
         * @return {@link List<${TabName}>}
         */
        @JSONField(serialize = false, deserialize = false)
        default List<${TabName}> get${TabName}ByIds(final Set<${idType}> ids) {
            return Lists.newArrayList(getAppContext().getBean(${JavaName}Repository.class).findAll(q.id.in(ids)));
        }
    }

${(await import('./repository/update')).updateAuth(table)}
${(await import('./repository/deleteById')).deleteByIdAuthSpare(table)}
${(await import('./repository/deleteByUid')).deleteByUidAuth(table)}
${(await import('./repository/markDeleteById')).markDeleteByIdAuth(table)}
${(await import('./repository/markDeleteByUid')).markDeleteByUidAuth(table)}
${(await import('./repository/markDeleteByIds')).markDeleteByIdsAuth(table)}
${(await import('./repository/markDelete')).markDeleteAuth(table)}
${(await import('./repository/findCacheById')).findCacheByIdAuthSpare(table)}
${(await import('./repository/findList')).findListAuth(table)}
${(await import('./repository/findListExpression')).findListExpressionAuth(table)}
${(await import('./repository/findPage')).findPageAuth(table)}
${(await import('./repository/findPageExpression')).findPageExpressionAuth(table)}
${(await import('./repository/findListProjection')).findListProjectionAuth(table)}
${(await import('./repository/findListProjectionExpression')).findListProjectionExpressionAuth(table)}
${(await import('./repository/findPageProjection')).findPageProjectionAuth(table)}
${(await import('./repository/findPageProjectionExpression')).findPageProjectionExpressionAuth(table)}
}
`;
};
