package com.ccx.demo.business.user.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.google.common.collect.Lists;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.support.mvc.entity.ITable;
import com.support.mvc.entity.IWhere;
import com.support.mvc.entity.IWhere.QdslWhere;
import com.support.mvc.entity.base.Sorts;
import com.support.mvc.entity.validated.IMarkDelete;
import com.support.mvc.entity.validated.ISave;
import com.support.mvc.entity.validated.IUpdate;
import com.utils.util.Dates;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ccx.demo.business.user.entity.QTabUserLogin.tabUserLogin;
import static com.support.mvc.enums.Code.ORDER_BY;

/**
 * 实体类：用户登录记录表
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
@ApiModel(description = "用户登录记录表")
@JSONType(orders = {"id", "userId", "ip", "timestamp"})
public class TabUserLogin implements ITable, IWhere<JPAUpdateClause, QdslWhere> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {IUpdate.class, IMarkDelete.class})
    @Positive
    @ApiModelProperty(value = "数据ID")
    private Long id;
    /**
     * 用户ID，tab_user.id
     */
    @NotNull(groups = {ISave.class})
    @Min(1)
    @DecimalMax("9223372036854776000")
    @ApiModelProperty(value = "用户ID，tab_user.id")
    private Long userId;
    /**
     * 登录IP
     */
    @NotNull(groups = {ISave.class})
    @Size(max = 15)
    @ApiModelProperty(value = "登录IP")
    private String ip;
    /**
     * 登录时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(insertable = false, updatable = false)
    @ApiModelProperty(value = "登录时间")
    private Timestamp timestamp;

    /**
     * 用户信息
     */
    @Transient
    @QueryTransient
    @ApiModelProperty(value = "用户信息")
    private TabUser user;
    /**
     * 查询区间
     */
    @Transient
    @QueryTransient
    @ApiModelProperty(value = "登录时间查询区间")
    private Dates.Range timestampRange;
    /**
     * 排序字段
     */
    @QueryTransient
    @Transient
    @ApiModelProperty(value = "查询排序字段，com.ccx.demo.code.userlogin.entity.TabUserLogin$OrderBy")
    private List<Sorts.Order> sorts;

// Enum Start **********************************************************************************************************

    /**
     * 枚举：定义排序字段
     */
    public enum OrderBy {
        // 按 id 排序可替代按创建时间排序
        id(tabUserLogin.id),
//        userId(tabUserLogin.userId),
//        ip(tabUserLogin.ip),
//        timestamp(tabUserLogin.timestamp),
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
    public QdslWhere where() {
        final QTabUserLogin q = tabUserLogin;
        return QdslWhere.of()
                .and(id, () -> q.id.eq(id))
                .and(userId, () -> q.id.eq(userId))
                .and(ip, () -> q.ip.eq(ip))
                .and(timestampRange, () -> q.timestamp.between(timestampRange.rebuild().getBegin(), timestampRange.getEnd()));
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

    /**
     * 获取查询实体与数据库表映射的所有字段,用于投影到 VO 类
     * 支持追加扩展字段,追加扩展字段一般用于连表查询
     *
     * @param appends {@link Expression}[] 追加扩展连表查询字段
     * @return {@link Expression}[]
     */
    public static Expression<?>[] allColumnAppends(final Expression<?>... appends) {
        final List<Expression<?>> columns = Lists.newArrayList(appends);
        final Class<?> clazz = tabUserLogin.getClass();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType().isPrimitive()) continue;
                final Object o = field.get(tabUserLogin);
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
<cache alias="ITabUserLoginCache">
    <key-type>java.lang.Long</key-type>
    <value-type>com.ccx.demo.code.userlogin.entity.TabUserLogin</value-type>
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
 * 缓存：用户登录记录表
 *
 * @author 谢长春 on 2020-03-08
 *\/
public interface ITabUserLoginCache extends ICache {
    String CACHE_ROW_BY_ID = "ITabUserLoginCache";

    /**
     * 按 ID 获取数据缓存行
     *
     * @param id {@link TabUserLogin#getId()}
     * @return {@link Optional<TabUserLogin>}
     *\/
    @JSONField(serialize = false, deserialize = false)
    default Optional<TabUserLogin> getTabUserLoginCacheById(final Long id) {
        return Optional.ofNullable(id)
                .filter(v -> v > 0)
                .map(v -> getAppContext().getBean(UserLoginRepository.class).findCacheById(v));
    }
}
*/
