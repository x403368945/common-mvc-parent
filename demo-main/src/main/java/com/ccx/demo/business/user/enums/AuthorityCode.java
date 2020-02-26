package com.ccx.demo.business.user.enums;

import com.ccx.demo.business.user.vo.Authority;
import com.google.common.collect.Sets;

import java.util.Arrays;

import static com.ccx.demo.business.user.enums.AuthorityType.*;

/**
 * <pre>
 * 枚举：权限代码：所有 Controller 方法头上加注解 @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{权限代码}')") 之后需要定义到这里
 * 强制定义在这里的原因是避免权限漏掉或匹配不到权限，且保证权限代码是唯一的
 *
 * 命名规则：
 *   Menu_*: 首页菜单相关的权限
 *   {*Controller}_*: Controller 方法头上注解，规则 【{Controller 全称}_{方法名} => UserController_deleteById】
 *
 * @author 谢长春 2019/8/27
 */
public enum AuthorityCode {
    // ------------------------------------------------------------------------------------------------- 枚举长度限制在这里，数据库字段长度不能超过 100
    Menu_Home("首页", MENU),

    // 系统设置 *********************************************************************************************************
    Menu_Setting("系统设置", MENU),

    // 用户角色权限 ******************************************************************************************************
    Menu_User("用户管理", MENU),
    UserController_page("查看数据", LOAD),
    UserController_save("新增数据", BUTTON),
    UserController_update("更新数据", BUTTON),
    UserController_reset("重置密码", BUTTON),

    Menu_Role("角色管理", MENU),
    RoleController_page("查看数据", LOAD),
    RoleController_save("新增数据", BUTTON),
    RoleController_update("更新数据", BUTTON),

    ;
    public final String comment;
    public final AuthorityType type;
    public final boolean deprecated;

    AuthorityCode(final String comment, final AuthorityType type) {
        this(comment, type, false);
    }

    AuthorityCode(final String comment, final AuthorityType type, final boolean deprecated) {
        this.comment = comment;
        this.type = type;
        this.deprecated = deprecated;
    }

    public Authority build() {
        return new Authority().setCode(this).setName(comment).setType(type);
    }

    public Authority nodes(final Authority... nodes) {
        return build().setNodes(Sets.newHashSet(nodes));
    }
}