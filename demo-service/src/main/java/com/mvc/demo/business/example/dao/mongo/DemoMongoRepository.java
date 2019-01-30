package com.mvc.demo.business.example.dao.mongo;

import com.mvc.demo.business.example.entity.DemoMongo;
import com.mvc.demo.enums.Radio;
import com.mvc.demo.config.init.BeanInitializer;
import com.support.mvc.dao.IRepository;
import com.support.mvc.entity.base.Pager;
import com.querydsl.core.QueryResults;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.mvc.demo.business.example.entity.DemoMongo.Props.*;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * 数据操作：
 *
 *
 * @author 谢长春 on 2019-01-03
 */
public interface DemoMongoRepository extends
        MongoRepository<DemoMongo, String>,
        IRepository<DemoMongo, String> {
//    QDemoMongo q = QDemoMongo.demoMongo; // 只有在使用 MorphiaQuery 时才需要

    @Override
    default long update(final String id, final Long userId, final DemoMongo obj) {
        return BeanInitializer.Beans.mongoTemplate.<MongoTemplate>get()
                .updateFirst(
                        // 注意【MongoDB】：如果 modifyTime 在 where 条件中，则日期格式必须精确到毫秒 yyyy-MM-dd HH:mm:ss.SSS，因为 Example 查询使用的是 equals 匹配；MySQL不存在该问题
                        new Query(byExample(Example.of(DemoMongo.builder().id(id).createUserId(userId).modifyTime(obj.getModifyTime()).build()))),
                        obj.update(new Update()).get(),
                        DemoMongo.class
                )
                .getModifiedCount();
    }

    @Override
    default DemoMongo deleteById(final String id, final Long userId) {
        // 只能删除自己创建的数据
        return findOne(Example.of(DemoMongo.builder().id(id).createUserId(userId).build()))
                .map(obj -> {
                    delete(obj);
                    return obj;
                })
                .orElseThrow(() -> new NullPointerException("数据物理删除失败：".concat(
                        DemoMongo.builder().id(id).createUserId(userId).build().json())
                ));
    }

    @Override
    default long markDeleteById(final String id, final Long userId) {
        return BeanInitializer.Beans.mongoTemplate.<MongoTemplate>get()
                .updateFirst(
                        new Query(byExample(Example.of(DemoMongo.builder().id(id).createUserId(userId).build()))),
                        new Update()
                                .set(deleted.name(), Radio.YES)
                                .set(modifyUserId.name(), userId)
                                .set(modifyTime.name(), Timestamp.valueOf(LocalDateTime.now()))
                        ,
                        DemoMongo.class
                )
                .getModifiedCount();
    }

    @Override
    default long markDeleteByIds(final List<String> ids, final Long userId) {
        return BeanInitializer.Beans.mongoTemplate.<MongoTemplate>get()
                .updateMulti(
                        new Query(where(id.name()).in(ids).and(createUserId.name()).is(userId)),
                        new Update()
                                .set(deleted.name(), Radio.YES)
                                .set(modifyUserId.name(), userId)
                                .set(modifyTime.name(), Timestamp.valueOf(LocalDateTime.now()))
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
