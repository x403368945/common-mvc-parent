package com.ccx.demo.business.example.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.ccx.demo.business.example.enums.DemoStatus;
import com.ccx.demo.business.user.cache.ITabUserCache;
import com.ccx.demo.enums.Radio;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.support.mvc.entity.ITable;
import com.support.mvc.entity.ITimestamp;
import com.support.mvc.entity.IWhere;
import com.support.mvc.entity.IWhere.QdslWhere;
import com.support.mvc.entity.base.Prop;
import com.support.mvc.entity.base.Sorts;
import com.support.mvc.entity.validated.IMarkDelete;
import com.support.mvc.entity.validated.ISave;
import com.support.mvc.entity.validated.IUpdate;
import com.utils.util.Dates;
import com.utils.util.Range;
import com.utils.util.Then;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ccx.demo.business.example.entity.QTabDemoList.tabDemoList;
import static com.support.mvc.entity.base.Prop.*;
import static com.support.mvc.entity.base.Prop.Type.*;
import static com.support.mvc.enums.Code.ORDER_BY;

/**
 * 实体类：
 *
 * @author 谢长春 on 2018-12-17.
 */
@Table(name = "tab_demo_list") // 注解映射数据库表名
@Entity // 声明数据库映射实体类，将会被Spring扫描
@QueryEntity // 注解为 QueryDsl 实体类，将会被 QueryDsl 通用查询框架扫描，生成Q{ClassName}.java
@DynamicInsert // 声明，编译生成 insert 语句时，当字段为 null ，则被忽略
@DynamicUpdate
// @DynamicUpdate
// 声明，编译生成 update 语句时，当字段为 null ，则被忽略；
// 这里有坑，当数据库字段有值，希望把数据库字段设置为 null，这种情况需要使用其他解决方案；
// 方案1：数据库设置时，数字默认 0，字符串默认 '' ；需要置空时，实体类设置属性为默认的 0 和 ''；
//   优点：代码量少逻辑简单；
//   缺点：JPA 只支持 ID 字段作为更新条件
// 方案2【推荐】：代码构建需要更新的字段，因为数据库有些字段可能不适合设置默认值
//   优点：更灵活，场景可适配；大部分场景下，只有ID作为更新匹配条件无法满足需求
//   缺点：代码量增加
@NoArgsConstructor // 生成无参构造函数
@AllArgsConstructor // 生成全参构造函数
@Builder // 生成链式类构造器
@Data // 生成 get & set & toString & hashCode & equals 方法
// @JSONType(orders ={})：声明实体类属性在 JSON 序列化时的排序；警告：必须声明所有返回字段的顺序，否则此声明不起作用；版本升级后再次测试发现该 bug 有修正，会对 orders 的字段优先，orders 不存在的字段才会乱序
@JSONType(orders = {"id", "uid", "name", "content", "amount", "status", "insertTime", "insertUserId", "insertUserName", "updateTime", "updateUserId", "updateUserName", "deleted"})
public class TabDemoList implements
//        ITabDemoList
        ITable, // 所有与数据库表 - 实体类映射的表都实现该接口；方便后续一键查看所有表的实体
        ITabUserCache, // 所有需要返回操作用户信息的，都实现此接口，会自动从缓存中获取操作用户信息
        ITimestamp, // 所有需要更新时间戳的实体类
        // JPAUpdateClause => IRepository#update 需要的动态更新字段；采用 方案2 时需要实现该接口
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
    @NotNull(groups = {IUpdate.class, IMarkDelete.class})
    @Size(min = 32, max = 32)
    private String uid;
    /**
     * 名称
     */
//    @NotNull(groups = {ISave.class, IUpdate.class}) // 数据库有设置 default ''
    @Size(max = 50)
    private String name;
    /**
     * 内容
     */
    @Size(max = 65535)
    private String content;
    /**
     * 金额
     */
    @Digits(integer = 18, fraction = 2)
    private Double amount;
    /**
     * 状态（0：无效，1：等待中，2：执行中，3：成功，4：失败）
     */
    private DemoStatus status;
    /**
     * 创建时间
     */
    @Column(insertable = false, updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss") // 声明 JSON 序列化和反序列化的格式
    private Timestamp insertTime;
    /**
     * 创建用户ID
     */
    @Column(updatable = false)
    @NotNull(groups = {ISave.class})
    @Positive
    private Long insertUserId;
    /**
     * 创建用户昵称
     */
    @Column(updatable = false)
    @NotNull(groups = {ISave.class})
    @Size(max = 30)
    private String insertUserName;
    /**
     * 修改时间
     */
    @Column(insertable = false, updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp updateTime;
    /**
     * 修改用户ID
     */
    @NotNull(groups = {ISave.class, IUpdate.class})
    @Positive
    private Long updateUserId;
    /**
     * 修改用户昵称
     */
    @NotNull(groups = {ISave.class, IUpdate.class})
    @Size(max = 30)
    private String updateUserName;
    /**
     * 是否逻辑删除（1、已删除， 0、未删除）
     */
    @Column(insertable = false, updatable = false)  // 声明当 JPA 执行 insert | update 语句时，就算该字段有值也会被忽略
    @Null(groups = {ISave.class})
    private Radio deleted;
    /**
     * 数字查询区间
     */
    @QueryTransient
    @Transient
    private Range<Double> amountRange;
    /**
     * 创建时间查询区间
     */
    @QueryTransient // 声明生成 Q{ClassName}.java 时忽略该属性
    @Transient // 声明 JPA + Hibernate 不与数据库建立映射，且 insert 和 update 忽略该属性
    private Dates.Range insertTimeRange;
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
        name(STRING.build(true, "名称")),
        content(STRING.build("内容")),
        amount(DOUBLE.build("金额")),
        status(ENUM.build(true, "状态").setOptions(DemoStatus.comments())),
        insertTime(TIMESTAMP.build("创建时间")),
        insertUserId(LONG.build("创建用户ID")),
        insertUserName(STRING.build("创建用户昵称")),
        updateTime(TIMESTAMP.build("修改时间")),
        updateUserId(LONG.build("修改用户ID")),
        updateUserName(STRING.build("修改用户昵称")),
        deleted(ENUM.build("是否逻辑删除").setOptions(Radio.comments())),
        timestamp(LONG.build("数据最后一次更新时间戳")),
        amountRange(RANGE_NUM.apply("金额查询区间")),
        insertTimeRange(RANGE_DATE.apply("创建时间查询区间")),
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
        id(tabDemoList.id),
        //		uid(tabDemoList.uid),
//		name(tabDemoList.name),
//		content(tabDemoList.content),
//		amount(tabDemoList.amount),
//		status(tabDemoList.status),
//		insertTime(tabDemoList.insertTime),
//		insertUserId(tabDemoList.insertUserId),
//		insertUserName(tabDemoList.insertUserName),
        updateTime(tabDemoList.updateTime),
//		updateUserId(tabDemoList.updateUserId),
//		updateUserName(tabDemoList.updateUserName),
//		deleted(tabDemoList.deleted)
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
        final QTabDemoList q = tabDemoList;
        // 动态拼接 update 语句
        // 以下案例中 只有 name 属性 为 null 时才不会加入 update 语句；
        return Then.of(jpaUpdateClause)
                // 当 name != null 时更新 name 属性
                .then(name, update -> update.set(q.name, name))
                .then(content, update -> update.set(q.content, content))
                .then(amount, update -> update.set(q.amount, amount))
                .then(status, update -> update.set(q.status, status))
                // 强制更新操作人信息
                .then(update -> update.set(q.updateUserId, updateUserId))
                .then(update -> update.set(q.updateUserName, updateUserName))

                // 假设数据库中 content is not null；可以在属性为null时替换为 ""
//                .then(update -> update.set(q.content, Optional.ofNullable(content).orElse("")))
                // 数据库中 amount 可以为 null
//                .then(update -> update.set(q.amount, amount))
                ;
    }

    @Override
    public QdslWhere where() {
        final QTabDemoList q = tabDemoList;
        // 构建查询顺序规则请参考：IWhere#where
        return QdslWhere.of()
                .and(status, () -> q.status.eq(status))
                .and(uid, () -> q.uid.eq(uid))
                .and(insertUserId, () -> q.insertUserId.eq(insertUserId))
                .and(updateUserId, () -> q.updateUserId.eq(updateUserId))
                // 强制带默认值的查询字段
                .and(q.deleted.eq(Objects.isNull(getDeleted()) ? Radio.NO : deleted))
                // 模糊匹配查询：后面带 % ；建议优先使用
                .and(name, () -> q.name.startsWith(name)) // 模糊匹配查询：后面带 %
                .and(content, () -> q.content.startsWith(content)) // 模糊匹配查询：后面带 %
                // 数字区间查询
                .and(amountRange, () -> q.amount.between(amountRange.getMin(), amountRange.getMax()))
                // 日期区间查询；Range.rebuild() : 先将时间区间重置到 00:00:00.000 - 23:59:59.999 ; 大多数情况都需要重置时间
                .and(insertTimeRange, () -> q.insertTime.between(insertTimeRange.rebuild().getBegin(), insertTimeRange.getEnd()))
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
