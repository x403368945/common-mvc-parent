package com.ccx.demo.business.example.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.ccx.demo.business.example.entity.convert.ArrayCodeJsonConvert;
import com.ccx.demo.business.user.cache.ITabUserCache;
import com.ccx.demo.enums.Bool;
import com.ccx.demo.business.user.cache.ITabUserCache;
import com.google.common.collect.Lists;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.support.mvc.entity.ITable;
import com.support.mvc.entity.IWhere;
import com.support.mvc.entity.IWhere.QdslWhere;
import com.support.mvc.entity.base.Item;
import com.support.mvc.entity.base.Sorts;
import com.support.mvc.entity.convert.ArrayItemJsonConvert;
import com.support.mvc.entity.convert.ArrayLongJsonConvert;
import com.support.mvc.entity.convert.ArrayStringJsonConvert;
import com.support.mvc.entity.convert.ItemJsonConvert;
import com.support.mvc.entity.validated.IMarkDelete;
import com.support.mvc.entity.validated.ISave;
import com.support.mvc.entity.validated.IUpdate;
import com.support.mvc.enums.Code;
import com.utils.util.Then;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ccx.demo.business.example.entity.QTabConvert.tabConvert;
import static com.support.mvc.enums.Code.ORDER_BY;

/**
 * 实体类：测试自定义 Convert 表
 *
 * @author 谢长春 on 2019-08-21
 */
@Table(name = "tab_convert")
@Entity
@QueryEntity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel(description = "测试自定义 Convert 表")
@JSONType(orders = {"id", "uid", "ids", "images", "codes", "items", "item", "insertTime", "insertUserId", "insertUserName", "updateTime", "updateUserId", "updateUserName", "deleted"})
public final class TabConvert implements
        ITable, // 所有与数据库表 - 实体类映射的表都实现该接口；方便后续一键查看所有表的实体
        ITabUserCache,
        // JPAUpdateClause => com.support.mvc.dao.IRepository#update 需要的动态更新字段；采用 方案2 时需要实现该接口
        // QdslWhere       => com.support.mvc.dao.IViewRepository 需要的查询条件
        IWhere<JPAUpdateClause, QdslWhere> {

    private static final long serialVersionUID = 5075904123381336930L;
    /**
     * 数据ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {IUpdate.class, IMarkDelete.class})
    @Positive
    @ApiModelProperty(value = "数据ID", position = 1)
    private Long id;
    /**
     * 数据UUID，缓存和按ID查询时可使用强校验
     */
    @Column(updatable = false)
    @NotNull(groups = {ISave.class, IUpdate.class, IMarkDelete.class})
    @Size(min = 32, max = 32)
    @ApiModelProperty(value = "数据uid", position = 2)
    private String uid;
    /**
     * {@link List<Long>}
     */
    @Convert(converter = ArrayLongJsonConvert.class)
    @ApiModelProperty(value = "{@link List<Long>}", position = 3)
    private Long[] ids;
    /**
     * {@link List<String>}
     */
    @Convert(converter = ArrayStringJsonConvert.class)
    @ApiModelProperty(value = "{@link List<String>}", position = 4)
    private String[] images;
    /**
     * {@link List<com.support.mvc.enums.Code>}
     */
    @Convert(converter = ArrayCodeJsonConvert.class)
    @ApiModelProperty(value = "{@link List<com.support.mvc.enums.Code>}", position = 5)
    private Code[] codes;
    /**
     * {@link List<com.support.mvc.entity.base.Item>}
     */
    @Convert(converter = ArrayItemJsonConvert.class)
    @ApiModelProperty(value = "{@link List<com.support.mvc.entity.base.Item>}", position = 6)
    private Item[] items;
    /**
     * {@link com.support.mvc.entity.base.Item}
     */
    @Convert(converter = ItemJsonConvert.class)
    @ApiModelProperty(value = "{@link com.support.mvc.entity.base.Item}", position = 7)
    private Item item;
    /**
     * 创建时间
     */
    @Column(insertable = false, updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Null(groups = {ISave.class})
    @ApiModelProperty(value = "数据新增时间", example = "2020-02-02 02:02:02", position = 8)
    private Timestamp insertTime;
    /**
     * 创建用户ID
     */
    @Column(updatable = false)
    @NotNull(groups = {ISave.class})
    @Positive
    @ApiModelProperty(value = "新增操作人id", position = 9)
    private Long insertUserId;
    /**
     * 修改时间
     */
    @Column(insertable = false, updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    @Null(groups = {ISave.class})
    @ApiModelProperty(value = "数据最后一次更新时间", example = "2020-02-02 02:02:02.002", position = 10)
    private Timestamp updateTime;
    /**
     * 修改用户ID
     */
    @NotNull(groups = {ISave.class, IUpdate.class})
    @Positive
    @ApiModelProperty(value = "更新操作人id", position = 11)
    private Long updateUserId;
    /**
     * 是否逻辑删除，参考：Enum{@link com.ccx.demo.enums.Bool}
     */
    @Column(insertable = false, updatable = false)
    @Null(groups = {ISave.class})
    @ApiModelProperty(value = "是否逻辑删除，com.ccx.demo.enums.Bool", position = 12)
    private Bool deleted;

    /**
     * 排序字段
     */
    @QueryTransient
    @Transient
    @ApiModelProperty(value = "查询排序字段，com.ccx.demo.code.convert.entity.TabConvert$OrderBy")
    private List<Sorts.Order> sorts;

// Enum Start **********************************************************************************************************

    /**
     * 枚举：定义排序字段
     */
    public enum OrderBy {
        // 按 id 排序可替代按创建时间排序
        id(tabConvert.id),
//        uid(tabConvert.uid),
//        ids(tabConvert.ids),
//        images(tabConvert.images),
//        codes(tabConvert.codes),
//        items(tabConvert.items),
//        item(tabConvert.item),
//        insertTime(tabConvert.insertTime),
//        insertUserId(tabConvert.insertUserId),
//        updateTime(tabConvert.updateTime),
//        updateUserId(tabConvert.updateUserId),
//        deleted(tabConvert.deleted),
        ;
        public final Sorts asc;
        public final Sorts desc;

        public Sorts get(final Sorts.Direction direction) {
            return Objects.equals(direction, Sorts.Direction.ASC) ? asc : desc;
        }

        public Sorts.Order asc() {
            return Sorts.Order.builder().name(this.name()).direction(Sorts.Direction.ASC).build();
        }

        public Sorts.Order desc() {
            return Sorts.Order.builder().name(this.name()).direction(Sorts.Direction.DESC).build();
        }

        /**
         * 获取所有排序字段名
         *
         * @return {@link String[]}
         */
        public static String[] names() {
            return Stream.of(OrderBy.values()).map(Enum::name).toArray(String[]::new);
        }

        OrderBy(final ComparableExpressionBase<?> qdsl) {
            asc = Sorts.asc(qdsl, this);
            desc = Sorts.desc(qdsl, this);
        }
    }

// Enum End : DB Start *************************************************************************************************

    @Override
    public Then<JPAUpdateClause> update(final JPAUpdateClause jpaUpdateClause) {
        final QTabConvert q = tabConvert;
        // 动态拼接 update 语句
        // 以下案例中 只有 name 属性 为 null 时才不会加入 update 语句；
        return Then.of(jpaUpdateClause)
                .then(ids, update -> update.set(q.ids, ids))
                .then(images, update -> update.set(q.images, images))
                .then(codes, update -> update.set(q.codes, codes))
                .then(items, update -> update.set(q.items, items))
                .then(item, update -> update.set(q.item, item))
                .then(updateUserId, update -> update.set(q.updateUserId, updateUserId))
//                // 当 name != null 时更新 name 属性
//                .then(name, update -> update.set(q.name, name))
//                .then(update -> update.set(q.updateUserId, updateUserId))
//                // 假设数据库中 content is not null；可以在属性为null时替换为 ""
//                .then(update -> update.set(q.content, Optional.ofNullable(content).orElse("")))
//                // 数据库中 amount 可以为 null
//                .then(update -> update.set(q.amount, amount))
                ;
    }

    @Override
    public QdslWhere where() {
        final QTabConvert q = tabConvert;
        // 构建查询顺序规则请参考：com.support.mvc.entity.IWhere#where
        return QdslWhere.of()
                .and(id, () -> q.id.eq(id))
                .and(uid, () -> q.uid.eq(uid))
                .and(ids, () -> Expressions.booleanTemplate("JSON_CONTAINS({0},{1})>0", q.ids, JSON.toJSONString(ids)))
                .and(images, () -> Expressions.booleanTemplate("JSON_CONTAINS({0},{1})>0", q.images, JSON.toJSONString(images)))
                .and(codes, () -> Expressions.booleanTemplate("JSON_CONTAINS({0},{1})>0", q.codes, JSON.toJSONString(codes)))
//                .and(items, () -> q.items.eq(items))
//                .and(item, () -> q.item.eq(item))
//                .and(insertTimeRange, () -> q.insertTime.between(insertTimeRange.rebuild().getBegin(), insertTimeRange.getEnd()))
//                .and(insertUserId, () -> q.insertUserId.eq(insertUserId))
//                .and(updateTimeRange, () -> q.updateTime.between(updateTimeRange.rebuild().getBegin(), updateTimeRange.getEnd()))
//                .and(updateUserId, () -> q.updateUserId.eq(updateUserId))
                .and(deleted, () -> q.deleted.eq(deleted))
//                .and(phone, () -> q.phone.eq(phone))
//                .and(insertUserId, () -> q.insertUserId.eq(insertUserId))
//                .and(updateUserId, () -> q.updateUserId.eq(updateUserId))
//                // 强制带默认值的查询字段
//                .and(q.deleted.eq(Objects.isNull(getDeleted()) ? Bool.NO : deleted))
//                // 数字区间查询
//                .and(amountRange, () -> q.amount.between(amountRange.getMin(), amountRange.getMax()))
//                // 日期区间查询；Range.rebuild() : 先将时间区间重置到 00:00:00.000 - 23:59:59.999 ; 大多数情况都需要重置时间
//                .and(insertTimeRange, () -> q.insertTime.between(insertTimeRange.rebuild().getBegin(), insertTimeRange.getEnd()))
//                // 模糊匹配查询：后面带 % ；建议优先使用
//                .and(name, () -> q.name.startsWith(name)) // 模糊匹配查询：后面带 %
//                .and(name, () -> q.name.endsWith(name)) // 模糊匹配查询：前面带 %
//                .and(name, () -> q.name.contains(name)) // 模糊匹配查询：前后带 %,同 MessageFormat.format("%{0}%", name)
//                .and(name, () -> q.name.like(MessageFormat.format("%{0}%", name))) 模糊匹配查询：前后带 %
                ;
    }

//    @Override
//    public List<Sorts> defaultSorts() {
//        return Collections.singletonList(OrderBy.id.desc); // 这里可以指定默认排序字段
//    }

    @Override
    public List<Sorts> parseSorts() {
        try {
            return Objects.isNull(sorts) ? null : sorts.stream().map(by -> OrderBy.valueOf(by.getName()).get(by.getDirection())).collect(Collectors.toList());
        } catch (Exception e) {
            throw ORDER_BY.exception("排序字段可选范围：".concat(JSON.toJSONString(OrderBy.names())));
        }
    }

    /**
     * 获取查询实体与数据库表映射的所有字段,用于投影到 VO 类
     * 支持追加扩展字段,追加扩展字段一般用于连表查询
     *
     * @param appends {@link Expression}[] 追加扩展连表查询字段
     * @return {@link Expression}[]
     */
    public static Expression<?>[] allColumnAppends(final Expression<?>... appends) {
        final List<Expression<?>> columns = Lists.newArrayList(appends);
        final Class<?> clazz = tabConvert.getClass();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType().isPrimitive()) continue;
                final Object o = field.get(tabConvert);
                if (o instanceof EntityPath || o instanceof BeanPath) continue;
                if (o instanceof Path) {
                    columns.add((Path<?>) o);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("获取查询实体属性与数据库映射的字段异常", e);
        }
        return columns.toArray(new Expression<?>[0]);
    }

// DB End **************************************************************************************************************

}
/* ehcache 配置
<cache alias="ITabConvertCache">
    <key-type>java.lang.Long</key-type>
    <value-type>com.ccx.demo.code.convert.entity.TabConvert</value-type>
    <expiry>
        <ttl unit="days">10</ttl>
    </expiry>
    <resources>
        <heap>100</heap>
        <offheap unit="MB">30</offheap>
    </resources>
</cache>
*/
/*
import com.alibaba.fastjson.annotation.JSONField;
import com.querydsl.core.annotations.QueryTransient;
import com.support.mvc.entity.ICache;
import java.beans.Transient;

import static com.ccx.demo.config.init.BeanInitializer.Beans.getAppContext;
/**
 * 缓存：测试自定义 Convert 表
 *
 * @author 谢长春 on 2020-03-11
 *\/
public interface ITabConvertCache extends ICache {
    String CACHE_ROW_BY_ID = "ITabConvertCache";

    /**
     * 按 ID 获取数据缓存行
     *
     * @param id {@link TabConvert#getId()}
     * @return {@link Optional<TabConvert>}
     *\/
    @JSONField(serialize = false, deserialize = false)
    default Optional<TabConvert> getTabConvertCacheById(final Long id) {
        return Optional.ofNullable(id)
                .filter(v -> v > 0)
                .map(v -> getAppContext().getBean(ConvertRepository.class).findCacheById(v));
    }
}
*/
