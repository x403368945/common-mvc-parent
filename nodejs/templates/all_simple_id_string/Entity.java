package <%=pkg%>.code.<%=javaname%>.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import <%=pkg%>.enums.Radio;
import <%=pkg%>.support.entity.ITabUserCache;
import com.support.mvc.entity.ITable;
import com.support.mvc.entity.ITimestamp;
import com.support.mvc.entity.IWhere;
import com.support.mvc.entity.IWhere.QdslWhere;
import com.support.mvc.entity.base.Prop;
import com.support.mvc.entity.base.Sorts;
import com.support.mvc.entity.validated.IMarkDelete;
import com.support.mvc.entity.validated.ISave;
import com.support.mvc.entity.validated.IUpdate;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAUpdateClause;
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

import static <%=pkg%>.code.<%=javaname%>.entity.Q<%=TabName%>.<%=tabName%>;
import static com.support.mvc.entity.base.Prop.*;
import static com.support.mvc.entity.base.Prop.Type.*;
import static com.support.mvc.enums.Code.ORDER_BY;

/**
 * 实体类：<%=comment%>
 *
 * @author 谢长春 on <%=date%>
 */
@Table(name = "<%=tab_name%>")
@Entity
@QueryEntity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JSONType(orders = {<%=orders%>})
public class <%=TabName%> implements
        ITable, // 所有与数据库表 - 实体类映射的表都实现该接口；方便后续一键查看所有表的实体
        <%=ITabUserCache%>
        <%=ITimestamp%>
        // JPAUpdateClause => com.support.mvc.dao.IRepository#update 需要的动态更新字段；采用 方案2 时需要实现该接口
        // QdslWhere       => com.support.mvc.dao.IViewRepository 需要的查询条件
        IWhere<JPAUpdateClause, QdslWhere>
{
<%=fields%>

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
<%=props%>,

//        timestamp(LONG.build("数据最后一次更新时间戳")),
//        numRange(RANGE_NUM.apply("数字查询区间")),
//        insertTimeRange(RANGE_DATE.apply("创建时间查询区间")),
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
<%=orderBy%>,
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
//        final Q<%=TabName%> q = <%=tabName%>;
//        // 动态拼接 update 语句
//        // 以下案例中 只有 name 属性 为 null 时才不会加入 update 语句；
//        return Then.of(jpaUpdateClause)
<%=update%>
////                // 当 name != null 时更新 name 属性
////                .then(name, update -> update.set(q.name, name))
////                .then(update -> update.set(q.updateUserId, updateUserId))
////                // 假设数据库中 content is not null；可以在属性为null时替换为 ""
////                .then(update -> update.set(q.content, Optional.ofNullable(content).orElse("")))
////                // 数据库中 amount 可以为 null
////                .then(update -> update.set(q.amount, amount))
//                ;
        return null;
    }

    @Override
    public QdslWhere where() {
//        final Q<%=TabName%> q = <%=tabName%>;
//        // 构建查询顺序规则请参考：com.support.mvc.entity.IWhere#where
//        return QdslWhere.of()
<%=where%>
////                .and(phone, () -> q.phone.eq(phone))
////                .and(insertUserId, () -> q.insertUserId.eq(insertUserId))
////                .and(updateUserId, () -> q.updateUserId.eq(updateUserId))
////                // 强制带默认值的查询字段
////                .and(q.deleted.eq(Objects.isNull(getDeleted()) ? Radio.NO : deleted))
////                // 数字区间查询
////                .and(amountRange, () -> q.amount.between(amountRange.getMin(), amountRange.getMax()))
////                // 日期区间查询；Range.rebuild() : 先将时间区间重置到 00:00:00.000 - 23:59:59.999 ; 大多数情况都需要重置时间
////                .and(insertTimeRange, () -> q.insertTime.between(insertTimeRange.rebuild().getBegin(), insertTimeRange.getEnd()))
////                // 模糊匹配查询：后面带 % ；建议优先使用
////                .and(name, () -> q.name.startsWith(name)) // 模糊匹配查询：后面带 %
////                .and(name, () -> q.name.endsWith(name)) // 模糊匹配查询：前面带 %
////                .and(name, () -> q.name.like(MessageFormat.format("%{0}%", name))) // 模糊匹配查询：前后带 %
//                ;
        return null;
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
/* ehcache 配置
<cache alias="I<%=TabName%>Cache">
    <key-type>java.lang.Long</key-type>
    <value-type><%=pkg%>.code.<%=javaname%>.entity.<%=TabName%></value-type>
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
import java.beans.Transient;

import static <%=pkg%>.config.init.BeanInitializer.Beans.getAppContext;
/**
 * 缓存：<%=comment%>
 *
 * @author 谢长春 on <%=date%>
 *\/
public interface I<%=TabName%>Cache {
    String CACHE_ROW_BY_ID = "I<%=TabName%>Cache";

    /**
     * 按 ID 获取数据缓存行
     *
     * @param id {@link <%=TabName%>#getId()}
     * @return {@link Optional<<%=TabName%>>}
     *\/
    @JSONField(serialize = false, deserialize = false)
    default Optional<<%=TabName%>> get<%=TabName%>CacheById(final Long id) {
        return Optional.of(getAppContext().getBean(<%=JavaName%>Repository.class).findCacheById(id));
    }
}
*/
