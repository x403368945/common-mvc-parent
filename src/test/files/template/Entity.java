package com.demo.business.{javaname}.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.demo.enums.Radio;
import com.demo.support.entity.IUser;
import com.mvc.entity.ITable;
import com.mvc.entity.ITimestamp;
import com.mvc.entity.IWhere;
import com.mvc.entity.IWhere.QdslWhere;
import com.mvc.entity.base.Prop;
import com.mvc.entity.base.Sorts;
import com.mvc.entity.validated.IMarkDelete;
import com.mvc.entity.validated.ISave;
import com.mvc.entity.validated.IUpdate;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.utils.util.Then;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.demo.business.{javaname}.entity.Q{TabName}.{tabName};
import static com.mvc.entity.base.Prop.*;
import static com.mvc.entity.base.Prop.Type.*;
import static com.mvc.enums.Code.ORDER_BY;

/**
 * 实体类：
 *
 * @author 谢长春 on {date}.
 */
@Table(name = "{tab_name}")
@Entity
@QueryEntity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JSONType(orders = {"{orders}"})
public class {TabName} implements
        ITable, // 所有与数据库表 - 实体类映射的表都实现该接口；方便后续一键查看所有表的实体
        {IUser}
        {ITimestamp}
        // JPAUpdateClause => com.mvc.dao.IRepository#update 需要的动态更新字段；采用 方案2 时需要实现该接口
        // QdslWhere       => com.mvc.dao.IViewRepository 需要的查询条件
        IWhere<JPAUpdateClause, QdslWhere>
{
{fields}

    /**
     * 排序字段
     */
    @QueryTransient
    @Transient
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
        {props},

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
{orderBy}
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
    public Then<JPAUpdateClause> update(final JPAUpdateClause jpaUpdateClause) {
//        final Q{TabName} q = {tabName};
//        // 动态拼接 update 语句
//        // 以下案例中 只有 name 属性 为 null 时才不会加入 update 语句；
//        return Then.of(jpaUpdateClause)
//                // 当 name != null 时更新 name 属性
//                .then(name, update -> update.set(q.name, name))
//                .then(update -> update.set(q.modifyUserId, modifyUserId))
//                // 假设数据库中 content is not null；可以在属性为null时替换为 ""
//                .then(update -> update.set(q.content, Optional.ofNullable(content).orElse("")))
//                // 数据库中 amount 可以为 null
//                .then(update -> update.set(q.amount, amount))
//                ;
        return null;
    }

    @Override
    public QdslWhere where() {
//        final Q{TabName} q = {tabName};
//        // 构建查询顺序规则请参考：com.mvc.entity.IWhere#qdslWhere
//        return QdslWhere.of()
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
////                .and(name, () -> q.name.endsWith(name)) // 模糊匹配查询：前面带 %
////                .and(name, () -> q.name.like(MessageFormat.format("%{0}%", name))) // 模糊匹配查询：前后带 %
//                ;
        return null;
    }

    @Override
    public List<Sorts> buildSorts() {
        try {
            return Objects.isNull(getSorts()) || getSorts().isEmpty()
                    ? null // Collections.singletonList(OrderBy.id.desc) // 若排序字段为空，这里可以设置默认按 id 倒序
                    : getSorts().stream().map(by -> OrderBy.valueOf(by.getName()).get(by.getDirection())).collect(Collectors.toList());
        } catch (Exception e) {
            throw ORDER_BY.exception("排序字段可选范围：".concat(JSON.toJSONString(OrderBy.names())));
        }
    }

// DB End **************************************************************************************************************

}
