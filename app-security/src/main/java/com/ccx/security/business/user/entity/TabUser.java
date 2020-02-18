package com.ccx.security.business.user.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.ccx.security.enums.Bool;
import com.ccx.security.enums.Role;
import com.support.mvc.entity.base.Prop;
import com.utils.IJson;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.support.mvc.entity.base.Prop.Type.*;

/**
 *
 * @author 谢长春
 */
//@Table(name = "tab_user")
//@Entity
//@QueryEntity
//@DynamicInsert
//@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"password"})
@JSONType(orders = {"id", "uid", "username", "nickname", "phone", "email", "role", "deleted"})
public class TabUser extends UserDetail implements IJson {

    /**
     * 实体类所有属性名
     * 当其他地方有用到字符串引用该类属性时，应该使用该枚举定义
     */
    public enum Props {
        id(LONG.build(true, "数据ID，主键自增")),
        uid(STRING.build("用户UUID，缓存和按ID查询时可使用强校验")),
        username(STRING.build(true, "登录名")),
        password(STRING.build(true, "登录密码")),
        nickname(STRING.build("昵称")),
        phone(STRING.build("手机号")),
        email(STRING.build("邮箱")),
        role(ENUM.build("角色").setOptions(Role.comments())),
        insertTime(TIMESTAMP.build("创建时间")),
        insertUserId(LONG.build("创建用户ID")),
        updateTime(TIMESTAMP.build("修改时间")),
        updateUserId(LONG.build("修改用户ID")),
        deleted(ENUM.build("是否逻辑删除").setOptions(Bool.comments()));
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户UUID，缓存和按ID查询时可使用强校验
     */
    @Column(updatable = false)
    private String uid;
    /**
     * 登录名
     */
    @Column(updatable = false)
    private String username;
    /**
     * 登录密码
     */
    @Column(updatable = false)
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
     */
    private Role role;
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
                .username(username)
                .nickname(nickname)
                .phone(phone)
                .email(email)
                .role(role)
                .build();
    }

}
