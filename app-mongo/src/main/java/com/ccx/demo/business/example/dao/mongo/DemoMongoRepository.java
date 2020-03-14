package com.ccx.demo.business.example.dao.mongo;

import com.alibaba.fastjson.annotation.JSONField;
import com.ccx.demo.business.example.entity.DemoMongo;
import com.ccx.demo.enums.Bool;
import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import com.support.mvc.dao.IRepository;
import com.support.mvc.entity.base.Pager;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ccx.demo.business.example.entity.DemoMongo.Props.*;
import static com.ccx.demo.config.init.BeanInitializer.Beans.mongoTemplate;
import static com.ccx.demo.config.init.BeanInitializer.getAppContext;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * 数据操作：
 *
 * @author 谢长春 on 2019-01-03
 */
public interface DemoMongoRepository extends
        MongoRepository<DemoMongo, String>,
        IRepository<DemoMongo, String> {
//    QDemoMongo q = QDemoMongo.demoMongo; // 只有在使用 MorphiaQuery 时才需要

    /**
     * 如果该表有缓存时请使用缓存，将这段代码注释，然后组合缓存接口。
     * 组合模式：定义表数据无缓存时，优化连表查询方法.
     * 实体类只需要组合该接口就可以获得按 id 查询方法.
     * 这种实现方式可以分解连表查询，减轻数据库压力，对分页查询有优化，让单表查询方法复用范围更广
     * 使用参考：
     * <pre>
     * public class TabEntity implements IDemoMongoRepository{
     *     public Long foreignKey;
     *     public Set<Long> foreignKeys;
     *
     *     public DemoMongo getForeign(){
     *         // 连表查询单条记录
     *         return getDemoMongoById(foreignKey).orElse(null);
     *     }
     *     public List<DemoMongo> getForeigns(){
     *         // 连表查询多条记录
     *         return getDemoMongoByIds(foreignKeys);
     *     }
     * }
     * </pre>
     */
    interface IDemoMongoRepository {
        /**
         * 按 ID 获取数据行，用于表数据无缓存时，优化连表查询
         *
         * @param id {@link DemoMongo#getId()}
         * @return {@link Optional <DemoMongo>}
         */
        @JSONField(serialize = false, deserialize = false)
        default Optional<DemoMongo> getDemoMongoById(final String id) {
            return getAppContext().getBean(DemoMongoRepository.class).findById(id);
        }

        /**
         * 按 ID 获取数据行，用于表数据无缓存时，优化连表查询
         *
         * @param ids {@link DemoMongo#getId()}
         * @return {@link List<DemoMongo>}
         */
        @JSONField(serialize = false, deserialize = false)
        default List<DemoMongo> getDemoMongoByIds(final Set<String> ids) {
            return Lists.newArrayList(getAppContext().getBean(DemoMongoRepository.class).findAllById(ids));
        }
    }

    //     @CacheEvict(cacheNames = IDemoMongoCache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码 <
    @Override
    default long update(final String id, final Long userId, final DemoMongo obj) {
        return mongoTemplate.<MongoTemplate>get()
                .updateFirst(
                        // 注意【MongoDB】：如果 updateTime 在 where 条件中，则日期格式必须精确到毫秒 yyyy-MM-dd HH:mm:ss.SSS，因为 Example 查询使用的是 equals 匹配；MySQL不存在该问题
                        new Query(byExample(Example.of(DemoMongo.builder().id(id).insertUserId(userId).updateTime(obj.getUpdateTime()).build()))),
                        obj.update(new Update()).get(),
                        DemoMongo.class
                )
                .getModifiedCount();
    }

    //     @CacheEvict(cacheNames = IDemoMongoCache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码 <
    @Override
    default DemoMongo deleteById(final String id, final Long userId) {
        // 只能删除自己创建的数据
        return findOne(Example.of(DemoMongo.builder().id(id).insertUserId(userId).build()))
                .map(obj -> {
                    delete(obj);
                    return obj;
                })
                .orElseThrow(() -> new NullPointerException("数据物理删除失败：".concat(
                        DemoMongo.builder().id(id).insertUserId(userId).build().json())
                ));
    }

    //     @CacheEvict(cacheNames = IDemoMongoCache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码 <
    @Override
    default long markDeleteById(final String id, final Long userId) {
        return mongoTemplate.<MongoTemplate>get()
                .updateFirst(
                        new Query(byExample(Example.of(DemoMongo.builder().id(id).insertUserId(userId).build()))),
                        new Update()
                                .set(deleted.name(), Bool.YES)
                                .set(updateUserId.name(), userId)
                                .set(updateTime.name(), Timestamp.valueOf(LocalDateTime.now()))
                        ,
                        DemoMongo.class
                )
                .getModifiedCount();
    }

    @Override
    default long markDeleteByIds(final Set<String> ids, final Long userId) {
        return mongoTemplate.<MongoTemplate>get()
                .updateMulti(
                        new Query(where(id.name()).in(ids).and(insertUserId.name()).is(userId)),
                        new Update()
                                .set(deleted.name(), Bool.YES)
                                .set(updateUserId.name(), userId)
                                .set(updateTime.name(), Timestamp.valueOf(LocalDateTime.now()))
                        ,
                        DemoMongo.class
                )
                .getModifiedCount();
    }

    @Override
    default List<DemoMongo> findList(final DemoMongo condition) {
        return (List<DemoMongo>) findAll(condition.where().toPredicate(), condition.buildJpaSort());
//        return (List<DemoMongo>) findAll(condition.where().toPredicate(), condition.buildQdslSorts());
    }

    @Override
    default QueryResults<DemoMongo> findPage(final DemoMongo condition, final Pager pager) {
        return pager.toQueryResults(
                findAll(condition.where().toPredicate(), pager.pageable(condition.buildJpaSort()))
        );
    }

}
