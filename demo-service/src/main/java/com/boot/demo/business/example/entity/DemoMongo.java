package com.boot.demo.business.example.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.boot.demo.business.example.enums.DemoStatus;
import com.boot.demo.enums.Radio;
import com.boot.demo.support.entity.IUser;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.types.OrderSpecifier;
import com.support.mvc.actions.IUpdate;
import com.support.mvc.entity.IMongo;
import com.support.mvc.entity.ITimestamp;
import com.support.mvc.entity.IWhere;
import com.support.mvc.entity.IWhere.QdslWhere;
import com.support.mvc.entity.base.Prop;
import com.support.mvc.entity.base.Sorts;
import com.support.mvc.entity.validated.IMarkDelete;
import com.support.mvc.entity.validated.ISave;
import com.utils.util.Dates;
import com.utils.util.Range;
import com.utils.util.Then;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.support.mvc.entity.base.Prop.SORTS;
import static com.support.mvc.entity.base.Prop.Type.*;
import static com.support.mvc.enums.Code.ORDER_BY;

/**
 * 实体类：
 *
 * @author 谢长春 on 2019-01-03.
 */
@Document
@QueryEntity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JSONType(orders = {"id", "name", "phone", "age", "status", "createTime", "createUserId", "modifyTime", "modifyUserId", "deleted"})
public class DemoMongo implements
        IMongo, // 所有与数据库表 - 实体类映射的表都实现该接口；方便后续一键查看所有表的实体
        IUser,
        ITimestamp, // 所有需要更新时间戳的实体类
        IWhere<Update, QdslWhere> // 声明用于实现构建 com.support.mvc.dao.IViewRepository 需要的查询条件
{
    /**
     * 数据ID，主键自增
     */
    @Id
    @NotBlank(groups = {ISave.class, IUpdate.class, IMarkDelete.class})
    private String id;
    /**
     * 姓名
     */
    @Indexed
    @NotNull
    @Size(max = 50)
    private String name;
    /**
     * 手机
     */
    @Indexed
    @Size(max = 50)
    private String phone;
    /**
     * 年龄
     */
    @Min(0)
    @Max(150)
    private Short age;
    /**
     * 状态（0：无效，1：等待中，2：执行中，3：成功，4：失败）
     */
    @Indexed
    @NotNull(groups = {ISave.class, IUpdate.class})
    private DemoStatus status;
    /**
     * 创建时间
     */
    @NotNull(groups = {ISave.class})
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    /**
     * 创建用户ID
     */
    @NotNull(groups = {ISave.class})
    @Positive
    private Long createUserId;
    /**
     * 修改时间
     */
    @NotNull(groups = {ISave.class, IUpdate.class})
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
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
    @Indexed
    @NotNull(groups = {ISave.class})
    private Radio deleted;

    /**
     * 年龄查询区间
     */
    @QueryTransient
    @org.springframework.data.annotation.Transient
    private Range<Short> ageRange;
    /**
     * 创建时间查询区间
     */
    @QueryTransient
    @org.springframework.data.annotation.Transient
    private Dates.Range createTimeRange;
    /**
     * 排序字段
     */
    @QueryTransient // 声明生成 Q{ClassName}.java 时忽略该属性
    @org.springframework.data.annotation.Transient // mongodb(spirng-data)忽略字段
    private List<Sorts.Order> sorts;

    @Override
    public String toString() {
        return json();
    }

// Enum Start **********************************************************************************************************

    /**
     * 实体类所有属性名
     * 当其他地方有用到字符串引用该类属性时，应该使用该枚举定义
     */
    public enum Props {
        id(LONG.build(true, "数据ID，主键自增")),
        name(STRING.build(true, "姓名")),
        phone(STRING.build("手机")),
        age(SHORT.build("年龄")),
        status(ENUM.build(true, "状态").setOptions(DemoStatus.comments())),
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
        //        id(demoMongo.id.asc(), demoMongo.id.desc()),
//        name(demoMongo.name.asc(), demoMongo.name.desc()),
//        phone(demoMongo.phone.asc(), demoMongo.phone.desc()),
//        age(demoMongo.age.asc(), demoMongo.age.desc()),
        createTime(com.boot.demo.business.example.entity.QDemoMongo.demoMongo.createTime.asc(), com.boot.demo.business.example.entity.QDemoMongo.demoMongo.createTime.desc()),
        //        createUserId(demoMongo.createUserId.asc(), demoMongo.createUserId.desc()),
        modifyTime(com.boot.demo.business.example.entity.QDemoMongo.demoMongo.modifyTime.asc(), com.boot.demo.business.example.entity.QDemoMongo.demoMongo.modifyTime.desc()),
//        modifyUserId(demoMongo.modifyUserId.asc(), demoMongo.modifyUserId.desc()),
//        deleted(demoMongo.deleted.asc(), demoMongo.deleted.desc())
        ;
        public final Sorts asc;
        public final Sorts desc;

        public Sorts get(final Sorts.Direction direction) {
            return Objects.equals(direction, Sorts.Direction.ASC) ? asc : desc;
        }

        /**
         * 获取所有排序字段名
         *
         * @return {@link String[]}
         */
        public static String[] names() {
            return Stream.of(OrderBy.values()).map(Enum::name).toArray(String[]::new);
        }

        OrderBy(final OrderSpecifier qdslAsc, final OrderSpecifier qdsldesc) {
            asc = Sorts.builder().qdsl(qdslAsc).jpa(Sort.Order.asc(this.name())).build();
            desc = Sorts.builder().qdsl(qdsldesc).jpa(Sort.Order.desc(this.name())).build();
        }
    }

// Enum End : DB Start *************************************************************************************************

    @Override
    public Then<Update> update(final Update mongoUpdate) {
        // 动态拼接 update 语句
        // 以下案例中 只有 name 属性 为 null 时才不会加入 update 语句；
        return Then.of(mongoUpdate)
                // 当 name 不为空时更新 name属性
                .then(name, update -> update.set(Props.name.name(), name))
                .then(update -> update.set(Props.modifyUserId.name(), modifyUserId))
                .then(update -> update.set(Props.modifyTime.name(), Timestamp.valueOf(LocalDateTime.now())))
                // 假设数据库中 content is not null；可以在属性为null时替换为 ""
                .then(update -> update.set(Props.phone.name(), Optional.ofNullable(phone).orElse("")))
                // 数据库中 amount 可以为 null
                .then(update -> update.set(Props.age.name(), age))
                ;
    }

    @Override
    public QdslWhere where() {
        final com.boot.demo.business.example.entity.QDemoMongo q = com.boot.demo.business.example.entity.QDemoMongo.demoMongo;
        // 构建查询顺序规则请参考：IWhere#where
        return QdslWhere.of()
                .and(phone, () -> q.phone.eq(phone))
                .and(createUserId, () -> q.createUserId.eq(createUserId))
                .and(modifyUserId, () -> q.modifyUserId.eq(modifyUserId))
                // 强制带默认值的查询字段
                .and(q.deleted.eq(Objects.isNull(getDeleted()) ? Radio.NO : deleted))
                // 年龄区间查询
                .and(ageRange, () -> q.age.between(ageRange.getMin(), ageRange.getMax()))
                // 日期区间查询；Range.rebuild() : 先将时间区间重置到 00:00:00.000 - 23:59:59.999 ; 大多数情况都需要重置时间
                .and(createTimeRange, () -> q.createTime.between(createTimeRange.rebuild().getBegin(), createTimeRange.getEnd()))
                // 模糊匹配查询：后面带 % ；建议优先使用
                .and(name, () -> q.name.startsWith(name)) // 模糊匹配查询：后面带 %
//                .and(name, () -> q.name.endsWith(name)) // 模糊匹配查询：前面带 %
//                .and(name, () -> q.name.like(MessageFormat.format("%{0}%", name))) // 模糊匹配查询：前后带 %
                ;
    }

    @Override
    public List<Sorts> buildSorts() {
        try {
            return Objects.isNull(getSorts()) || getSorts().isEmpty()
                    ? Collections.singletonList(OrderBy.createTime.desc) // Collections.singletonList(OrderBy.id.desc) // 若排序字段为空，这里可以设置默认按 id 倒序
                    : getSorts().stream().map(by -> OrderBy.valueOf(by.getName()).get(by.getDirection())).collect(Collectors.toList());
        } catch (Exception e) {
            throw ORDER_BY.exception("排序字段可选范围：".concat(JSON.toJSONString(OrderBy.names())));
        }
    }

// DB End **************************************************************************************************************

}
