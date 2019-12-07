package com.ccx.demo.business.example.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.ccx.demo.business.example.entity.convert.ItemJsonConvert;
import com.ccx.demo.business.example.entity.convert.ListItemJsonConvert;
import com.ccx.demo.business.example.entity.convert.ListLongJsonConvert;
import com.ccx.demo.business.example.entity.convert.ListStringJsonConvert;
import com.ccx.demo.enums.Radio;
import com.ccx.demo.support.entity.IUser;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.annotations.QueryType;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.support.mvc.entity.ITable;
import com.support.mvc.entity.ITimestamp;
import com.support.mvc.entity.IWhere;
import com.support.mvc.entity.IWhere.QdslWhere;
import com.support.mvc.entity.base.Item;
import com.support.mvc.entity.base.Prop;
import com.support.mvc.entity.base.Sorts;
import com.support.mvc.entity.validated.IMarkDelete;
import com.support.mvc.entity.validated.ISave;
import com.support.mvc.entity.validated.IUpdate;
import com.utils.util.Then;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

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
import static com.support.mvc.entity.base.Prop.SORTS;
import static com.support.mvc.entity.base.Prop.Type.*;
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
@JSONType(orders = {"id", "uid", "ids", "images", "items", "item", "createTime", "createUserId", "createUserName", "modifyTime", "modifyUserId", "modifyUserName", "deleted"})
public final class TabConvert implements
        ITable, // 所有与数据库表 - 实体类映射的表都实现该接口；方便后续一键查看所有表的实体
        IUser,
        ITimestamp, // 所有需要更新时间戳的实体类
        // JPAUpdateClause => com.support.mvc.dao.IRepository#update 需要的动态更新字段；采用 方案2 时需要实现该接口
        // QdslWhere       => com.support.mvc.dao.IViewRepository 需要的查询条件
        IWhere<JPAUpdateClause, QdslWhere> {
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
//    @Convert(converter = ListLongJsonConvert.class)
    @Type(type = "Long[]")
    private Long[] ids;
    /**
     * {@link List<String>}
     */
    @Convert(converter = ListStringJsonConvert.class)
    private List<String> images;
    /**
     * {@link List<com.support.mvc.entity.base.Item>}
     */
    @Convert(converter = ListItemJsonConvert.class)
    private List<Item> items;
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
    private Timestamp createTime;
    /**
     * 创建用户ID
     */
    @Column(updatable = false)
    @NotNull(groups = {ISave.class})
    @Positive
    private Long createUserId;
    /**
     * 修改时间
     */
    @Column(insertable = false, updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    @Null(groups = {ISave.class})
    private Timestamp modifyTime;
    /**
     * 修改用户ID
     */
    @NotNull(groups = {ISave.class, IUpdate.class})
    @Positive
    private Long modifyUserId;
    /**
     * 是否逻辑删除（1、已删除， 0、未删除）
     */
    @Column(insertable = false, updatable = false)
    @Null(groups = {ISave.class})
    private Radio deleted;

    /**
     * 排序字段
     */
    @QueryTransient
    @Transient
    private List<Sorts.Order> sorts;

// Enum Start **********************************************************************************************************

    /**
     * 实体类所有属性名
     * 当其他地方有用到字符串引用该类属性时，应该使用该枚举定义
     */
    public enum Props {
        id(LONG.build(true, "数据ID，主键自增")),
        uid(STRING.build(true, "数据UUID，缓存和按ID查询时可使用强校验")),
        ids(ARRAY.build("{@link List<Long>}")),
        images(ARRAY.build("{@link List<String>}")),
        items(ARRAY.build("{@link List<com.support.mvc.entity.base.Item>}")),
        item(OBJECT.build("{@link com.support.mvc.entity.base.Item}")),
        createTime(TIMESTAMP.build("创建时间")),
        createUserId(LONG.build("创建用户ID")),
        modifyTime(TIMESTAMP.build("修改时间")),
        modifyUserId(LONG.build("修改用户ID")),
        deleted(ENUM.build("是否逻辑删除").setOptions(Radio.comments())),

        //        timestamp(LONG.build("数据最后一次更新时间戳")),
//        numRange(RANGE_NUM.apply("数字查询区间")),
//        createTimeRange(RANGE_DATE.apply("创建时间查询区间")),
        sorts(SORTS.apply(OrderBy.names())),
        ;
        private final Prop prop;

        public Prop getProp() {
            return prop;
        }

        Props(final Prop prop) {
            prop.setName(this.name());
            this.prop = prop;
        }

        public static List<Prop> list() {
            return Stream.of(Props.values()).map(Props::getProp).collect(Collectors.toList());
        }
    }

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
//        createTime(tabConvert.createTime),
//        createUserId(tabConvert.createUserId),
//        modifyTime(tabConvert.modifyTime),
//        modifyUserId(tabConvert.modifyUserId),
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
                .then(items, update -> update.set(q.items, items))
                .then(item, update -> update.set(q.item, item))
                .then(modifyUserId, update -> update.set(q.modifyUserId, modifyUserId))
//                // 当 name != null 时更新 name 属性
//                .then(name, update -> update.set(q.name, name))
//                .then(update -> update.set(q.modifyUserId, modifyUserId))
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
                .and(uid, () -> uid.endsWith("%") || uid.startsWith("%") ? q.uid.like(uid) : q.uid.eq(uid))
//                .and(ids, () -> q.ids.eq(ids))
//                .and(images, () -> q.images.eq(images))
//                .and(items, () -> q.items.eq(items))
//                .and(item, () -> q.item.eq(item))
//                .and(createTimeRange, () -> q.createTime.between(createTimeRange.rebuild().getBegin(), createTimeRange.getEnd()))
//                .and(createUserId, () -> q.createUserId.eq(createUserId))
//                .and(modifyTimeRange, () -> q.modifyTime.between(modifyTimeRange.rebuild().getBegin(), modifyTimeRange.getEnd()))
//                .and(modifyUserId, () -> q.modifyUserId.eq(modifyUserId))
                .and(deleted, () -> q.deleted.eq(deleted))
//                .and(phone, () -> q.phone.eq(phone))
//                .and(createUserId, () -> q.createUserId.eq(createUserId))
//                .and(modifyUserId, () -> q.modifyUserId.eq(modifyUserId))
//                // 强制带默认值的查询字段
//                .and(q.deleted.eq(Objects.isNull(getDeleted()) ? Radio.NO : deleted))
//                // 数字区间查询
//                .and(amountRange, () -> q.amount.between(amountRange.getMin(), amountRange.getMax()))
//                // 日期区间查询；Range.rebuild() : 先将时间区间重置到 00:00:00.000 - 23:59:59.999 ; 大多数情况都需要重置时间
//                .and(createTimeRange, () -> q.createTime.between(createTimeRange.rebuild().getBegin(), createTimeRange.getEnd()))
//                // 模糊匹配查询：后面带 % ；建议优先使用
//                .and(name, () -> q.name.startsWith(name)) // 模糊匹配查询：后面带 %
//                .and(name, () -> q.name.endsWith(name)) // 模糊匹配查询：前面带 %
//                .and(name, () -> q.name.like(MessageFormat.format("%{0}%", name))) // 模糊匹配查询：前后带 %
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
