package com.ccx.security.enums;

import com.support.mvc.entity.base.Item;
import com.utils.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 角色定义
 *
 * @author 谢长春
 */
@Slf4j
public enum Role {
    ROLE_ADMIN("超级管理员"),
    ROLE_SYS_ADMIN("系统管理员"),
    ROLE_USER("普通用户"),
    ROLE_VIP("VIP会员"),
    ;

    /**
     * 枚举属性说明
     */
    final String comment;

    Role(String comment) {
        this.comment = comment;
    }

    public GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(this.name());
    }

    /**
     * 构建选项集合
     *
     * @return {@link Item[]}
     */
    public static Item[] names() {
        return Stream.of(Role.values())
                .map(item -> Item.builder().key(item.name()).value(item.ordinal()).comment(item.comment).build())
                .toArray(Item[]::new);
    }

    /**
     * 转换为 {@link Item} 对象
     *
     * @return {@link Item}
     */
    public Item getObject() {
        return Item.builder()
                .key(this.name())
                .value(this.ordinal())
                .comment(this.comment)
                .build();
    }

    /**
     * 删除(ROLE_)前缀
     *
     * @param role Role
     * @return String
     */
    public static String role(Role role) {
        return role.name().substring(5);
    }

    /**
     * 删除(ROLE_)前缀
     *
     * @param roles Role[]
     * @return List<String>
     */
    public static List<String> any(Role... roles) {
        return Stream.of(roles)
                .map(Role::role)
                .collect(Collectors.toList());
    }

    public static boolean in(Role role, Role... roles) {
        return Util.in(role, Arrays.asList(roles));
    }

    public static void main(String[] args) {
        System.out.println(Stream.of(Role.values()).map(role -> String.format("%s【%d】:%s", role.name(), role.ordinal(), role.comment)).collect(Collectors.joining(",")));
        log.debug("@Secured({\"" + String.join("\",\"", Util.toStringArray(Role.values())) + "\"})");
        // 判断密码是否匹配
        log.debug("密码是否匹配：" + new BCryptPasswordEncoder().matches(
                "admin",
                "$2a$10$b9v.0glE7vYsrP9z.VMtV.ZRmBn05B1RgU3vFEjJ0O/E2wP7mjB8u"));
        { // 按角色生成新增用户 sql 脚本
            log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 按角色生成新增用户 sql 脚本 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
        { // 按角色生成新增机构用户 sql 脚本
//            ROLE_USER.showSQL("公司管理员", "orgadmin","10010000000", "111111", "c9d6ad96-3eed-4d70-879b-bead504f0730");
        }
//        log.debug("{}", Role.in(ROLE_USER, ROLE_ADMIN, ROLE_SYS_ADMIN, ROLE_USER));
//        log.debug("{}", Role.in(ROLE_USER, ROLE_ADMIN, ROLE_SYS_ADMIN));
        log.debug("密码是否匹配：" + new BCryptPasswordEncoder().matches(
                "111111",
                "$2a$10$HhrrWmpM.J/3d2JqPIEQvOYvsOOnE5bEqzPbqehqzuDVU0okD2F7m"));
        log.debug(new BCryptPasswordEncoder().encode("111111"));
    }
}
