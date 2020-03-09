package com.ccx.demo.business.example.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.ccx.demo.business.example.entity.convert.ArrayCodeJsonConvert;
import com.ccx.demo.business.user.cache.ITabUserCache;
import com.ccx.demo.enums.Bool;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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
    private Long id;
    /**
     * 数据UUID，缓存和按ID查询时可使用强校验
     */
    @Column(updatable = false)
    @NotNull(groups = {IUpdate.class, IMarkDelete.class})
    @Size(min = 32, max = 32)
    private String uid;
    /**
     * {@link List<Long>}
     */
    @Convert(converter = ArrayLongJsonConvert.class)
    private Long[] ids;
    /**
     * {@link List<String>}
     */
    @Convert(converter = ArrayStringJsonConvert.class)
    private String[] images;
    /**
     * {@link List<com.support.mvc.enums.Code>}
     */
    @Convert(converter = ArrayCodeJsonConvert.class)
    private Code[] codes;
    /**
     * {@link List<com.support.mvc.entity.base.Item>}
     */
    @Convert(converter = ArrayItemJsonConvert.class)
    private Item[] items;
    /**
     * {@link com.support.mvc.entity.base.Item}
     */
    @Convert(converter = ItemJsonConvert.class)
    private Item item;
    /**
     * 创建时间
     */
    @Column(insertable = false, updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    @Null(groups = {ISave.class})
    private Timestamp insertTime;
    /**
     * 创建用户ID
     */
    @Column(updatable = false)
    @NotNull(groups = {ISave.class})
    @Positive
    private Long insertUserId;
    /**
     * 修改时间
     */
    @Column(insertable = false, updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    @Null(groups = {ISave.class})
    private Timestamp updateTime;
    /**
     * 修改用户ID
     */
    @NotNull(groups = {ISave.class, IUpdate.class})
    @Positive
    private Long updateUserId;
    /**
     * 是否逻辑删除（1、已删除， 0、未删除）
     */
    @Column(insertable = false, updatable = false)
    @Null(groups = {ISave.class})
    private Bool deleted;

    /**
     * 排序字段
     */
    @QueryTransient
    @Transient
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
//                .and(name, () -> q.name.like(MessageFormat.format("%{0}%", name)))
//                .and(name, () -> q.name.contains(name)) // 模糊匹配查询：前后带 %,同 MessageFormat.format("%{0}%", name)
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

// DB End **************************************************************************************************************

}
