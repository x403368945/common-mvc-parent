-- *********************************************************************************************************************
-- 建议将 utf8 改为 utf8mb4
-- *********************************************************************************************************************
-- utf8mb4_unicode_ci和utf8mb4_general_ci的对比：
-- 准确性：
--   utf8mb4_unicode_ci是基于标准的Unicode来排序和比较，能够在各种语言之间精确排序
--   utf8mb4_general_ci没有实现Unicode排序规则，在遇到某些特殊语言或者字符集，排序结果可能不一致。
--   但是，在绝大多数情况下，这些特殊字符的顺序并不需要那么精确。
-- 性能
--   utf8mb4_general_ci在比较和排序的时候更快
--   utf8mb4_unicode_ci在特殊情况下，Unicode排序规则为了能够处理特殊字符的情况，实现了略微复杂的排序算法。
--   但是在绝大多数情况下发，不会发生此类复杂比较。相比选择哪一种collation，使用者更应该关心字符集与排序规则在db里需要统一。
-- *********************************************************************************************************************
-- SHOW VARIABLES WHERE Variable_name LIKE 'character_set_%' OR Variable_name LIKE 'collation%';
-- character_set_client	    (客户端来源数据使用的字符集)
-- character_set_connection	(连接层字符集)
-- character_set_database	(当前选中数据库的默认字符集)
-- character_set_results	(查询结果字符集)
-- character_set_server	    (默认的内部操作字符集)
-- *********************************************************************************************************************
/*
DROP DATABASE IF EXISTS master_slave_db;
CREATE DATABASE master_slave_db CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';
*/
-- 用户表
DROP TABLE IF EXISTS tab_user;
CREATE TABLE tab_user (
    `id`             BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '数据ID，主键自增',
    `uid`            VARCHAR(32)                       NOT NULL COMMENT '用户UUID，缓存和按ID查询时可使用强校验',
    `subdomain`      VARCHAR(10)                       NOT NULL DEFAULT '' COMMENT '子域名用户组',
    `username`       VARCHAR(15)                       NOT NULL COMMENT '登录名',
    `password`       VARCHAR(150)                      NOT NULL COMMENT '登录密码',
    `nickname`       VARCHAR(30)                       NOT NULL DEFAULT '' COMMENT '昵称',
    `phone`          VARCHAR(11)                       NOT NULL DEFAULT '' COMMENT '手机号',
    `email`          VARCHAR(30)                       NOT NULL DEFAULT '' COMMENT '邮箱',
    `role`           TINYINT(2) UNSIGNED               NOT NULL COMMENT '角色',
    `registerSource` TINYINT(1) UNSIGNED               NOT NULL DEFAULT 0 COMMENT '账户注册渠道',
    `createTime`     TIMESTAMP                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `createUserId`   BIGINT                            NOT NULL COMMENT '创建用户ID',
    `modifyTime`     TIMESTAMP(3)                      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    `modifyUserId`   BIGINT                            NOT NULL COMMENT '修改用户ID',
    `deleted`        TINYINT(1) UNSIGNED               NOT NULL DEFAULT 0 COMMENT '是否逻辑删除（1、已删除， 0、未删除）',
    KEY (`username`),
    KEY (`phone`),
    KEY (`email`)
)
    ENGINE InnoDB
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci COMMENT '用户表';

-- 用户登录记录表
DROP TABLE IF EXISTS tab_user_login;
CREATE TABLE tab_user_login (
    `id`        BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '数据ID，主键自增',
    `userId`    BIGINT                            NOT NULL COMMENT '用户ID，tab_user.id',
    `timestamp` TIMESTAMP(3)                      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '登录时间',
    KEY (`timestamp`)
)
    ENGINE InnoDB
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci COMMENT '用户登录记录表';


-- 测试表
DROP TABLE IF EXISTS tab_demo_list;
CREATE TABLE tab_demo_list (
    `id`             BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '数据ID，主键自增',
    `uid`            CHAR(32)                          NOT NULL COMMENT '数据UUID，缓存和按ID查询时可使用强校验',
    `name`           VARCHAR(50)                       NOT NULL DEFAULT '' COMMENT '名称',
    `content`        TEXT                              NULL COMMENT '内容',
    `amount`         DECIMAL(18, 2)                    NULL COMMENT '金额',
    `status`         TINYINT(2) UNSIGNED               NOT NULL DEFAULT 0 COMMENT '状态（0：无效，1：等待中，2：执行中，3：成功，4：失败）',
    `createTime`     TIMESTAMP                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `createUserId`   BIGINT                            NOT NULL COMMENT '创建用户ID',
    `createUserName` VARCHAR(30)                       NOT NULL COMMENT '创建用户昵称',
    `modifyTime`     TIMESTAMP(3)                      NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
    `modifyUserId`   BIGINT                            NOT NULL COMMENT '修改用户ID',
    `modifyUserName` VARCHAR(30)                       NOT NULL COMMENT '修改用户昵称',
    `deleted`        TINYINT(1) UNSIGNED               NOT NULL DEFAULT 0 COMMENT '是否逻辑删除（1、已删除， 0、未删除）',
    KEY (`uid`)
)
    ENGINE InnoDB
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci COMMENT '测试案例表';

