package com.ccx.demo.enums;

/**
 * 枚举：数据库主从切换
 *
 * @author 谢长春 2019/6/9
 */
public enum DBRoute {
    MASTER("主数据库连接：写、读（必要情况在方法头上加 @Master 注解，强制从主库读数据）"),
    SECOND("从数据库连接：读（优先级高）"),
    THIRD("从数据库连接：读（优先级高）"),
    ;
    final String comment;

    DBRoute(final String comment) {
        this.comment = comment;
    }
}
