package com.ccx.demo.business.user.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.ccx.demo.business.common.vo.UserFileInfo;
import com.ccx.demo.business.common.vo.convert.UserFileInfoJsonConvert;
import com.ccx.demo.business.user.cache.ITabUserCache;
import com.ccx.demo.business.user.vo.UserDetail;
import com.ccx.demo.enums.Bool;
import com.ccx.demo.enums.RegisterSource;
import com.ccx.demo.open.auth.cache.TokenCache;
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
import com.support.mvc.entity.base.Sorts;
import com.support.mvc.entity.convert.ArrayLongJsonConvert;
import com.support.mvc.entity.validated.IMarkDelete;
import com.support.mvc.entity.validated.ISave;
import com.support.mvc.entity.validated.IUpdate;
import com.utils.util.Then;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ccx.demo.business.user.entity.QTabUser.tabUser;
import static com.support.mvc.enums.Code.ORDER_BY;

/**
 * 用户实体
 *
 * @author 谢长春
 */
//@Table(name = "tab_user", uniqueConstraints = {@UniqueConstraint(columnNames = "uid")})
@Table(name = "tab_user")
@ApiModel(description = "用户表")
@Entity
@QueryEntity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"password"})
@JSONType(orders = {"id", "uid", "subdomain", "username", "nickname", "phone", "email", "avatar", "roles", "registerSource", "insertTime", "insertUserId", "updateTime", "updateUserId", "deleted"})
public class TabUser extends UserDetail implements ITable, ITabUserCache, IWhere<JPAUpdateClause, QdslWhere> {

    private static final long serialVersionUID = 1945320644170494162L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {IUpdate.class, IMarkDelete.class})
    @Positive
    @ApiModelProperty(value = "数据ID", position = 1)
    private Long id;
    /**
     * 用户UUID，缓存和按ID查询时可使用强校验
     */
    @Column(updatable = false)
    @NotNull(groups = {ISave.class, IUpdate.class, IMarkDelete.class})
    @Size(min = 32, max = 32)
    @ApiModelProperty(value = "数据uid", position = 2)
    private String uid;
    /**
     * 子域名用户组
     */
    @Column(updatable = false)
    @NotNull(groups = {ISave.class})
    @Size(max = 10)
    @ApiModelProperty(value = "子域名用户组", position = 3)
    private String domain;
    /**
     * 登录名
     */
    @NotNull(groups = {ISave.class})
    @Size(max = 15)
    @ApiModelProperty(value = "登录名", position = 4)
    private String username;
    /**
     * 登录密码
     */
    @Column(updatable = false)
    @NotNull(groups = {ISave.class})
    @Size(max = 150)
    @ApiModelProperty(value = "登录密码", position = 5)
    private String password;
    /**
     * 用户昵称
     */
    @NotNull(groups = {ISave.class})
    @Size(max = 30)
    @ApiModelProperty(value = "昵称", position = 6)
    private String nickname;
    /**
     * 手机号
     */
    @Column(updatable = false)
    @NotNull(groups = {ISave.class})
    @Size(max = 11)
    @ApiModelProperty(value = "手机号", position = 7)
    private String phone;
    /**
     * 邮箱
     */
    @Column(updatable = false)
    @NotNull(groups = {ISave.class})
    @Size(max = 30)
    @ApiModelProperty(value = "邮箱", position = 8)
    private String email;
    /**
     * 用户头像
     */
    @Convert(converter = UserFileInfoJsonConvert.class)
    @ApiModelProperty(value = "用户头像", position = 9)
    private UserFileInfo avatar;
    /**
     * 角色 ID 集合，tab_role.id，{@link Long}[]
     * 角色 ID 集合，tab_role.id {@link TabRole#getId()}
     */
    @Convert(converter = ArrayLongJsonConvert.class)
    @ApiModelProperty(value = "角色 ID 集合，tab_role.id，{@link Long}[]", position = 10)
    private Long[] roles;
    /**
     * 注册渠道
     */
    @Column(updatable = false)
    @ApiModelProperty(value = "账户注册渠道", hidden = true, position = 11)
    private RegisterSource registerSource;
    /**
     * 创建时间
     */
    @Column(insertable = false, updatable = false)
    @JSONField(serialize = false, deserialize = false, format = "yyyy-MM-dd HH:mm:ss")
    @Null(groups = {ISave.class})
    @ApiModelProperty(value = "数据新增时间", example = "2020-02-02 02:02:02", position = 12)
    private Timestamp insertTime;
    /**
     * 创建用户ID
     */
    @Column(updatable = false)
    @JSONField(serialize = false, deserialize = false)
    @NotNull(groups = {ISave.class})
    @Positive
    @ApiModelProperty(value = "新增操作人id", position = 13)
    private Long insertUserId;
    /**
     * 修改时间
     */
    @Column(insertable = false, updatable = false)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    @Null(groups = {ISave.class})
    @ApiModelProperty(value = "数据最后一次更新时间", example = "2020-02-02 02:02:02.002", position = 14)
    private Timestamp updateTime;
    /**
     * 修改用户ID
     */
    @NotNull(groups = {ISave.class, IUpdate.class})
    @Positive
    @ApiModelProperty(value = "更新操作人id", position = 15)
    private Long updateUserId;
    /**
     * 是否逻辑删除，参考：Enum{@link com.ccx.demo.enums.Bool}
     */
    @Column(insertable = false, updatable = false)
    @Null(groups = {ISave.class})
    @ApiModelProperty(value = "是否逻辑删除，com.ccx.demo.enums.Bool", position = 16)
    private Bool deleted;

    /**
     * 排序字段
     */
    @QueryTransient
    @Transient
    @ApiModelProperty(value = "查询排序字段，com.ccx.demo.code.user.entity.TabUser$OrderBy", position = 17)
    private List<Sorts.Order> sorts;
    /**
     * 新增用户时，选择的角色集合，经过验证之后，将角色 ID 保存到 {@link TabUser#roles}
     */
    @NotEmpty(groups = {ISave.class, IUpdate.class})
    @QueryTransient
    @Transient
    @ApiModelProperty(value = "角色集合，新增用户时，选择的角色集合，经过验证之后，才保存角色 ID ", position = 18)
    private Set<TabRole> roleList;

    @Override
    public TabUser loadUserDetail() {
        return this;
    }

    /**
     * 枚举：定义排序字段
     */
    public enum OrderBy {
        // 按 id 排序可替代按创建时间排序
        id(tabUser.id),
//        uid(tabUser.uid),
//        domain(tabUser.domain),
//        username(tabUser.username),
//        password(tabUser.password),
//        nickname(tabUser.nickname),
//        phone(tabUser.phone),
//        email(tabUser.email),
//        avatar(tabUser.avatar),
//        roles(tabUser.roles),
//        registerSource(tabUser.registerSource),
//        insertTime(tabUser.insertTime),
//        insertUserId(tabUser.insertUserId),
//        updateTime(tabUser.updateTime),
//        updateUserId(tabUser.updateUserId),
        deleted(tabUser.deleted),
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
            return Stream.of(TabUser.OrderBy.values()).map(Enum::name).toArray(String[]::new);
        }

        OrderBy(final ComparableExpressionBase<?> qdsl) {
            asc = Sorts.asc(qdsl, this);
            desc = Sorts.desc(qdsl, this);
        }
    }

    // DB Start ********************************************************************************************************
    @Override
    public Then<JPAUpdateClause> update(final JPAUpdateClause jpaUpdateClause) {
        final QTabUser q = tabUser;
        return Then.of(jpaUpdateClause)
                .then(nickname, update -> update.set(q.nickname, nickname))
                .then(phone, update -> update.set(q.phone, phone))
                .then(email, update -> update.set(q.email, email))
                .then(avatar, update -> update.set(q.avatar, avatar))
                .then(roles, update -> update.set(q.roles, roles))
                .then(update -> update.set(q.updateUserId, updateUserId))
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
                .and(domain, () -> q.domain.eq(domain))
                .and(registerSource, () -> q.registerSource.eq(registerSource))
                .and(insertUserId, () -> q.insertUserId.eq(insertUserId))
                .and(updateUserId, () -> q.updateUserId.eq(updateUserId))
                .and(q.deleted.eq(Objects.isNull(getDeleted()) ? Bool.NO : deleted))
                .and(nickname, () -> q.nickname.containsIgnoreCase(nickname))
//              Expressions.booleanTemplate("JSON_CONTAINS({0},{1})>0", q.roles, roleId)
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
    /**
     * 获取查询实体与数据库表映射的所有字段,用于投影到 VO 类
     * 支持追加扩展字段,追加扩展字段一般用于连表查询
     *
     * @param appends {@link Expression}[] 追加扩展连表查询字段
     * @return {@link Expression}[]
     */
    public static Expression<?>[] allColumnAppends(final Expression<?>... appends) {
        final List<Expression<?>> columns = Lists.newArrayList(appends);
        final Class<?> clazz = tabUser.getClass();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType().isPrimitive()) continue;
                final Object o = field.get(tabUser);
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
<cache alias="ITabUserCache">
    <key-type>java.lang.Long</key-type>
    <value-type>com.ccx.demo.code.user.entity.TabUser</value-type>
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
 * 缓存：用户表
 *
 * @author 谢长春 on 2020-03-08
 *\/
public interface ITabUserCache extends ICache {
    String CACHE_ROW_BY_ID = "ITabUserCache";

    /**
     * 按 ID 获取数据缓存行
     *
     * @param id {@link TabUser#getId()}
     * @return {@link Optional<TabUser>}
     *\/
    @JSONField(serialize = false, deserialize = false)
    default Optional<TabUser> getTabUserCacheById(final Long id) {
        return Optional.ofNullable(id)
                .filter(v -> v > 0)
                .map(v -> getAppContext().getBean(UserRepository.class).findCacheById(v));
    }
}
*/
