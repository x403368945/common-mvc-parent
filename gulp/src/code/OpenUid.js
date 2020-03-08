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
${(await import('./http/page')).pageOpen(table)}
${(await import('./http/search')).searchOpenSpare(table)}
${(await import('./http/findById')).findByIdOpenSpare(table)}
${(await import('./http/findByUid')).findByUidOpen(table)}
${(await import('./http/save')).saveOpen(table)}
${(await import('./http/update')).updateOpen(table)}
${(await import('./http/deleteById')).deleteByIdOpenSpare(table)}
${(await import('./http/deleteByUid')).deleteByUidOpenSpare(table)}
${(await import('./http/markDeleteByUid')).markDeleteByUidOpen(table)}
${(await import('./http/markDeleteByIds')).markDeleteByIdsOpenSpare(table)}
${(await import('./http/markDelete')).markDeleteOpen(table)}
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
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Result;
import com.support.mvc.web.IController;
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
public class ${JavaName}Controller implements IController<${idType}, ${TabName}> {

    private final ${JavaName}Service service;

${(await import('./controller/save')).saveOpen(table)}
${(await import('./controller/update')).updateOpen(table)}
${(await import('./controller/deleteById')).deleteByIdOpenSpare(table)}
${(await import('./controller/deleteByUid')).deleteByUidOpenSpare(table)}
${(await import('./controller/markDeleteByUid')).markDeleteByUidOpen(table)}
${(await import('./controller/markDeleteByIds')).markDeleteByIdsOpenSpare(table)}
${(await import('./controller/markDelete')).markDeleteOpen(table)}
${(await import('./controller/findById')).findByIdOpenSpare(table)}
${(await import('./controller/findByUid')).findByUidOpen(table)}
${(await import('./controller/page')).pageOpen(table)}
${(await import('./controller/search')).searchOpenSpare(table)}
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
import com.support.mvc.service${idType === DataType.VARCHAR.java ? '.str' : ''}.IOpenService;
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
public class ${JavaName}Service implements IOpenService<${TabName}>
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

${(await import('./service/save')).saveOpen(table)}
${(await import('./service/saveAll')).saveAllOpen(table)}
${(await import('./service/update')).updateOpen(table)}
${(await import('./service/deleteById')).deleteByIdOpenSpare(table)}
${(await import('./service/deleteByUid')).deleteByUidOpenSpare(table)}
${(await import('./service/markDeleteById')).markDeleteByIdOpenSpare(table)}
${(await import('./service/markDeleteByUid')).markDeleteByUidOpen(table)}
${(await import('./service/markDeleteByIds')).markDeleteByIdsOpenSpare(table)}
${(await import('./service/markDelete')).markDeleteOpen(table)}
${(await import('./service/findById')).findByIdOpenSpare(table)}
${(await import('./service/findByUid')).findByUidOpen(table)}
${(await import('./service/findPage')).findPageOpen(table)}
${(await import('./service/findList')).findListOpenSpare(table)}
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

${(await import('./repository/update')).updateOpen(table)}
${(await import('./repository/deleteById')).deleteByIdOpenSpare(table)}
${(await import('./repository/deleteByUid')).deleteByUidOpen(table)}
${(await import('./repository/markDeleteById')).markDeleteByIdOpen(table)}
${(await import('./repository/markDeleteByUid')).markDeleteByUidOpen(table)}
${(await import('./repository/markDeleteByIds')).markDeleteByIdsOpen(table)}
${(await import('./repository/markDelete')).markDeleteOpen(table)}
${(await import('./repository/findCacheById')).findCacheByIdOpenSpare(table)}
${(await import('./repository/findList')).findListOpen(table)}
${(await import('./repository/findListExpression')).findListExpressionOpen(table)}
${(await import('./repository/findPage')).findPageOpen(table)}
${(await import('./repository/findPageExpression')).findPageExpressionOpen(table)}
${(await import('./repository/findListProjection')).findListProjectionOpen(table)}
${(await import('./repository/findListProjectionExpression')).findListProjectionExpressionOpen(table)}
${(await import('./repository/findPageProjection')).findPageProjectionOpen(table)}
${(await import('./repository/findPageProjectionExpression')).findPageProjectionExpressionOpen(table)}
}
`;
};
