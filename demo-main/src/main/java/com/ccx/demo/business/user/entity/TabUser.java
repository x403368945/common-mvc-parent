package com.ccx.demo.business.user.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.ccx.demo.business.user.entity.extend.ITabUser;
import com.ccx.demo.enums.Radio;
import com.ccx.demo.enums.RegisterSource;
import com.ccx.demo.business.user.cache.ITabUserCache;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.annotations.QueryType;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.support.mvc.entity.ITable;
import com.support.mvc.entity.IWhere;
import com.support.mvc.entity.IWhere.QdslWhere;
import com.support.mvc.entity.base.Sorts;
import com.support.mvc.entity.convert.MysqlListLongConvert;
import com.support.mvc.entity.validated.ISave;
import com.support.mvc.entity.validated.IUpdate;
import com.utils.util.Then;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ccx.demo.business.user.entity.QTabUser.tabUser;
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
@JSONType(orders = {"id", "uid", "subdomain", "username", "nickname", "phone", "email", "role", "registerSource", "deleted"})
public class TabUser extends UserDetail implements ITabUser, ITable, ITabUserCache, IWhere<TabUser, QdslWhere> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户UUID，缓存和按ID查询时可使用强校验
     */
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
    @JSONField(serialize = false, deserialize = false)
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
    @Convert(converter = MysqlListLongConvert.class)
    @QueryType(PropertyType.STRING)
    private List<Long> roles;
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
    private Timestamp createTime;
    /**
     * 创建用户ID
     */
    @JSONField(serialize = false, deserialize = false)
    @Column(updatable = false)
    private Long createUserId;
    /**
     * 修改时间
     */
    @JSONField(serialize = false, deserialize = false, format = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(insertable = false, updatable = false)
    private Timestamp modifyTime;
    /**
     * 修改用户ID
     */
    @JSONField(serialize = false, deserialize = false)
    @Column(updatable = false)
    private Long modifyUserId;
    /**
     * 是否有效
     */
    @Column(insertable = false, updatable = false)
    private Radio deleted;
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
    private List<TabRole> roleList;
    /**
     * 权限指令集合
     */
    @QueryTransient
    @Transient
    private List<String> authorityList;

    @Override
    public String toString() {
        return json();
    }

    // DB Start *************************************************************************************************
    @Override
    public Then<TabUser> update(final TabUser update) {
        return Then.of(update)
                .then(nickname, dest -> dest.setNickname(nickname))
                .then(phone, dest -> dest.setPhone(phone))
                .then(email, dest -> dest.setEmail(email))
                .then(roles, dest -> dest.setRoles(roles))
                .then(dest -> dest.setModifyUserId(modifyUserId))
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
                .and(createUserId, () -> q.createUserId.eq(createUserId))
                .and(modifyUserId, () -> q.modifyUserId.eq(modifyUserId))
                .and(q.deleted.eq(Objects.isNull(getDeleted()) ? Radio.NO : deleted))
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

// DB End **************************************************************************************************************

    @Override
    public TabUser loadUserDetail() {
        return this;
    }

    /**
     * 构造登录返回字段
     *
     * @return TabUser
     */
    @Override
    public TabUser toLoginResult() {
        return TabUser.builder()
                .id(id)
                .uid(uid)
                .subdomain(subdomain)
                .username(username)
                .nickname(nickname)
                .phone(phone)
                .email(email)
                .authorityList(
                        Objects.nonNull(authorityList) && !authorityList.isEmpty()
                                ? authorityList
                                : Objects.requireNonNull(getRoles(), "当前登录账户未配置权限").stream()
                                .flatMap(id -> getRoleAuthoritiesCacheById(id).stream())
                                .distinct()
                                .collect(Collectors.toList())
                )
                .build();
    }

}
