-- DROP DATABASE IF EXISTS demo_main_db;
-- CREATE DATABASE demo_main_db CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
-- 用户表
DROP TABLE IF EXISTS tab_user;
CREATE TABLE tab_user (
  `id`             BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT '数据ID，主键自增',
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
  `modifyTime`     TIMESTAMP                         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `modifyUserId`   BIGINT                            NOT NULL COMMENT '修改用户ID',
  `deleted`        TINYINT(1) UNSIGNED               NOT NULL DEFAULT 0 COMMENT '是否逻辑删除（1、已删除， 0、未删除）',
  KEY (`username`),
  KEY (`phone`),
  KEY (`email`)
) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '用户表';

-- 用户登录记录表
DROP TABLE IF EXISTS tab_user_login;
CREATE TABLE tab_user_login (
  `id`        BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT '数据ID，主键自增',
  `userId`    BIGINT                            NOT NULL COMMENT '用户ID，tab_user.id',
  `timestamp` TIMESTAMP                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  KEY (`timestamp`)
) ENGINE InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '用户登录记录表';

