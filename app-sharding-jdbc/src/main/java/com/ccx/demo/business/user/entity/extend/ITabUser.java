package com.ccx.demo.business.user.entity.extend;

import com.ccx.demo.enums.Radio;
import com.ccx.demo.enums.RegisterSource;
import com.ccx.demo.enums.Role;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.support.mvc.entity.base.Prop;
import com.support.mvc.entity.base.Sorts;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ccx.demo.business.user.entity.QTabUser.tabUser;
import static com.support.mvc.entity.base.Prop.Type.*;

/**
 * 实体类扩展延伸
 *
 *
 * @author 谢长春 2018/12/20
 */
public interface ITabUser {
    /**
     * 实体类所有属性名
     * 当其他地方有用到字符串引用该类属性时，应该使用该枚举定义
     */
    enum Props {
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
            return Stream.of(Props.values()).map(Props::getProp).collect(Collectors.toList());
        }

        public static List<Prop> list(final Props... props) {
            return Stream.of(props).map(Props::getProp).collect(Collectors.toList());
        }
    }

    /**
     * 枚举：定义排序字段
     */
    enum OrderBy {
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
            return Stream.of(OrderBy.values()).map(Enum::name).toArray(String[]::new);
        }

        OrderBy(final ComparableExpressionBase<?> qdsl) {
            asc = Sorts.asc(qdsl, this);
            desc = Sorts.desc(qdsl, this);
        }
    }

}
