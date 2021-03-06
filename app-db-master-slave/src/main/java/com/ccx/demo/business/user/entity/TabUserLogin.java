package com.ccx.demo.business.user.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.support.mvc.entity.ITable;
import com.support.mvc.entity.IWhere;
import com.support.mvc.entity.IWhere.QdslWhere;
import com.support.mvc.entity.base.Prop;
import com.support.mvc.entity.base.Sorts;
import com.utils.util.Dates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ccx.demo.business.user.entity.QTabUserLogin.tabUserLogin;
import static com.support.mvc.entity.base.Prop.RANGE_DATE;
import static com.support.mvc.entity.base.Prop.SORTS;
import static com.support.mvc.entity.base.Prop.Type.*;
import static com.support.mvc.enums.Code.ORDER_BY;

/**
 * 实体：用户登录记录表
 *
 * @author 谢长春 on 2018/2/2.
 */
@Table(name = "tab_user_login")
@Entity
@QueryEntity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JSONType(orders = {"id", "userId", "timestamp"})
public class TabUserLogin implements ITable, IWhere<JPAUpdateClause, QdslWhere> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户ID
     */
    @Column(updatable = false)
    private Long userId;
    /**
     * 登录时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(insertable = false, updatable = false)
    private Timestamp timestamp;

    /**
     * 用户信息
     */
    @Transient
    @QueryTransient
    private TabUser user;
    /**
     * 查询区间
     */
    @Transient
    @QueryTransient
    private Dates.Range timestampRange;
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
        userId(LONG.build(true, "用户ID，tab_user.id")),
        insertTime(TIMESTAMP.build("登录时间")),
        user(OBJECT.build("用户信息").setProps(TabUser.Props.list())),
        insertTimeRange(RANGE_DATE.apply("登录时间查询区间")),
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
        id(tabUserLogin.id),
        timestamp(tabUserLogin.timestamp),
        ;
        public final Sorts asc;
        public final Sorts desc;

        public Sorts get(final Sorts.Direction direction) {
            return Objects.equals(direction, Sorts.Direction.DESC) ? desc : asc;
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
    public QdslWhere where() {
        final QTabUserLogin q = tabUserLogin;
        return QdslWhere.of()
                .and(id, () -> q.id.eq(id))
                .and(userId, () -> q.id.eq(userId))
                .and(timestampRange, () -> {
                    timestampRange.rebuild();
                    return q.timestamp.between(timestampRange.rebuild().getBegin(), timestampRange.getEnd());
                });
    }

    @Override
    public List<Sorts> defaultSorts() {
        return Collections.singletonList(OrderBy.id.desc);
    }

    @Override
    public List<Sorts> parseSorts() {
        try {
            return Objects.isNull(sorts) ? null : sorts.stream().map(by -> OrderBy.valueOf(by.getName()).get(by.getDirection())).collect(Collectors.toList());
        } catch (Exception e) {
            throw ORDER_BY.exception("排序字段可选范围：".concat(JSON.toJSONString(OrderBy.names())));
        }
    }

// DB End **************************************************************************************************************

    public static void main(String[] args) {
        System.out.println(OrderBy.id.get(Sorts.Direction.DESC));
    }
}
