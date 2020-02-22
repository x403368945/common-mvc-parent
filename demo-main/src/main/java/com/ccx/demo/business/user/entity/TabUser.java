package com.ccx.demo.business.user.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.ccx.demo.business.user.cache.ITabUserCache;
import com.ccx.demo.business.user.vo.UserDetail;
import com.ccx.demo.enums.Bool;
import com.ccx.demo.enums.RegisterSource;
import com.ccx.demo.enums.Role;
import com.ccx.demo.open.auth.cache.TokenCache;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.support.mvc.entity.ITable;
import com.support.mvc.entity.IWhere;
import com.support.mvc.entity.IWhere.QdslWhere;
import com.support.mvc.entity.base.Prop;
import com.support.mvc.entity.base.Sorts;
import com.support.mvc.entity.convert.ArrayLongJsonConvert;
import com.support.mvc.entity.validated.ISave;
import com.support.mvc.entity.validated.IUpdate;
import com.utils.util.Then;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ccx.demo.business.user.entity.QTabUser.tabUser;
import static com.support.mvc.entity.base.Prop.Type.*;
import static com.support.mvc.enums.Code.ORDER_BY;

/**
 * 用户实体
 *
 * @author 谢长春
 */
//@Table(name = "tab_user", uniqueConstraints = {@UniqueConstraint(columnNames = "uid")})
@Table(name = "tab_user")
@Entity
@QueryEntity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"password"})
@JSONType(orders = {"id", "uid", "subdomain", "username", "nickname", "phone", "email", "role", "registerSource", "deleted"})
public class TabUser extends UserDetail implements ITable, ITabUserCache, IWhere<JPAUpdateClause, QdslWhere> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户UUID，缓存和按ID查询时可使用强校验
     */
    @NotNull(groups = {ISave.class})
    @Size(min = 32, max = 32)
    @Column(updatable = false)
    private String uid;
    /**
     * 子域名用户组
     */
    @Column(updatable = false)
    private String subdomain;
    /**
     * 登录名
     */
    @Column(updatable = false)
    @NotBlank(groups = {ISave.class})
    private String username;
    /**
     * 登录密码
     */
    @Column(updatable = false)
    @NotBlank(groups = {ISave.class})
    @JSONField(serialize = false)
    private String password;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 手机号
     */
    @Column(updatable = false)
    private String phone;
    /**
     * 邮箱
     */
    @Column(updatable = false)
    private String email;

    /**
     * 用户角色
     * {@link TabRole#getId()}
     */
    @Convert(converter = ArrayLongJsonConvert.class)
    private Long[] roles;
    /**
     * 注册渠道
     */
    @Column(updatable = false)
    private RegisterSource registerSource;
    /**
     * 创建时间
     */
    @JSONField(serialize = false, deserialize = false, format = "yyyy-MM-dd HH:mm:ss")
    @Column(insertable = false, updatable = false)
    private Timestamp insertTime;
    /**
     * 创建用户ID
     */
    @JSONField(serialize = false, deserialize = false)
    @Column(updatable = false)
    private Long insertUserId;
    /**
     * 修改时间
     */
    @JSONField(serialize = false, deserialize = false, format = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(insertable = false, updatable = false)
    private Timestamp updateTime;
    /**
     * 修改用户ID
     */
    @JSONField(serialize = false, deserialize = false)
    @Column(updatable = false)
    private Long updateUserId;
    /**
     * 是否有效
     */
    @Column(insertable = false, updatable = false)
    private Bool deleted;
    /**
     * 排序字段
     */
    @QueryTransient
    @Transient
    private List<Sorts.Order> sorts;
    /**
     * 新增用户时，选择的角色集合，经过验证之后，将角色 ID 保存到 {@link TabUser#roles}
     */
    @NotEmpty(groups = {ISave.class, IUpdate.class})
    @QueryTransient
    @Transient
    private Set<TabRole> roleList;

    @Override
    public TabUser loadUserDetail() {
        return this;
    }

    /**
     * 实体类所有属性名
     * 当其他地方有用到字符串引用该类属性时，应该使用该枚举定义
     */
    public enum Props {
        id(LONG.build(true, "数据ID，主键自增")),
        uid(STRING.build("用户UUID，缓存和按ID查询时可使用强校验")),
        subdomain(STRING.build("子域名用户组")),
        username(STRING.build(true, "登录名")),
        password(STRING.build(true, "登录密码")),
        nickname(STRING.build("昵称")),
        phone(STRING.build("手机号")),
        email(STRING.build("邮箱")),
        role(ENUM.build("角色")),
        registerSource(ENUM.build("注册渠道")),
        insertTime(TIMESTAMP.build("创建时间")),
        insertUserId(LONG.build("创建用户ID")),
        updateTime(TIMESTAMP.build("修改时间")),
        updateUserId(LONG.build("修改用户ID")),
        deleted(ENUM.build("是否逻辑删除"));
        private final Prop prop;

        public Prop getProp() {
            return prop;
        }

        Props(final Prop prop) {
            prop.setName(this.name());
            this.prop = prop;
        }

        public static List<Prop> list() {
            return Stream.of(TabUser.Props.values()).map(TabUser.Props::getProp).collect(Collectors.toList());
        }

        public static List<Prop> list(final Props... props) {
            return Stream.of(props).map(TabUser.Props::getProp).collect(Collectors.toList());
        }
    }

    /**
     * 枚举：定义排序字段
     */
    public enum OrderBy {
        id(tabUser.id),
        // 按 id 排序可替代按创建时间排序
//        insertTime(tabUser.insertTime),
        updateTime(tabUser.updateTime),
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
            return Stream.of(TabUser.OrderBy.values()).map(Enum::name).toArray(String[]::new);
        }

        OrderBy(final ComparableExpressionBase<?> qdsl) {
            asc = Sorts.asc(qdsl, this);
            desc = Sorts.desc(qdsl, this);
        }
    }

// DB Start ********************************************************************************************************
    @Override
    public Then<JPAUpdateClause> update(final JPAUpdateClause update) {
        final QTabUser q = tabUser;
        return Then.of(update)
                .then(nickname, dest -> dest.set(q.nickname, nickname))
                .then(phone, dest -> dest.set(q.phone, phone))
                .then(email, dest -> dest.set(q.email, email))
                .then(roles, dest -> dest.set(q.roles, roles))
                .then(dest -> dest.set(q.updateUserId, updateUserId))
                ;
    }

    @Override
    public QdslWhere where() {
        final QTabUser q = tabUser;
        return QdslWhere.of()
                .and(id, () -> q.id.eq(id))
                .and(uid, () -> q.uid.eq(uid))
                .and(username, () -> q.username.eq(username))
                .and(phone, () -> q.phone.eq(phone))
                .and(email, () -> q.email.eq(email))
                .and(subdomain, () -> q.subdomain.eq(subdomain))
                .and(insertUserId, () -> q.insertUserId.eq(insertUserId))
                .and(updateUserId, () -> q.updateUserId.eq(updateUserId))
                .and(q.deleted.eq(Objects.isNull(getDeleted()) ? Bool.NO : deleted))
                .and(nickname, () -> q.nickname.containsIgnoreCase(nickname))
//              Expressions.booleanTemplate("JSON_CONTAINS({0},{1})>0",q.roles,roleId)
//              Expressions.booleanTemplate("JSON_CONTAINS({0},{1})>0", q.roles, JSON.toJSONString(roles))
                .and(roles, () -> Expressions.booleanTemplate("JSON_CONTAINS({0},{1})>0", q.roles, JSON.toJSONString(roles)))
                ;
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
     * 基于用户生成 token，每个用户只支持一个 token ，每次调用该方法，前一次生成的 token 将失效
     *
     * @return {@link String} 生成的 token
     */
    public String token() {
        return TokenCache.builder()
                .userId(this.id)
                .build()
                .token();
    }

// DB End **************************************************************************************************************
}
