-- mysql + mongo 镜像
-- DROP TABLE IF EXISTS tab_demo_jpa_mongo;
-- CREATE TABLE tab_demo_jpa_mongo (
--   `id`             BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT '数据ID，主键自增',
--   `uid`            CHAR(32)                          NOT NULL COMMENT '数据UUID，缓存和按ID查询时可使用强校验',
--   `name`           VARCHAR(50)                       NOT NULL COMMENT '姓名',
--   `phone`          VARCHAR(50)                       NOT NULL DEFAULT '' COMMENT '手机',
--   `age`            TINYINT(3) UNSIGNED               NULL COMMENT '年龄',
--   `createTime`     TIMESTAMP                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
--   `createUserId`   BIGINT                            NOT NULL COMMENT '创建用户ID',
--   `modifyTime`     TIMESTAMP                         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
--   `modifyUserId`   BIGINT                            NOT NULL COMMENT '修改用户ID',
--   `deleted`        TINYINT(1) UNSIGNED               NOT NULL DEFAULT 0 COMMENT '是否逻辑删除（1、已删除， 0、未删除）'
-- ) ENGINE InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '测试案例表：将mysql数据与mongodb同步';

-- 测试表
DROP TABLE IF EXISTS tab_demo_list;
CREATE TABLE tab_demo_list (
  `id`             BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT '数据ID，主键自增',
  `uid`            CHAR(32)                          NOT NULL COMMENT '数据UUID，缓存和按ID查询时可使用强校验',
  `name`           VARCHAR(50)                       NOT NULL DEFAULT '' COMMENT '名称',
  `content`        TEXT                              NULL COMMENT '内容',
  `amount`         DECIMAL(18, 2)                    NULL COMMENT '金额',
  `status`         TINYINT(2) UNSIGNED               NOT NULL DEFAULT 0 COMMENT '状态（0：无效，1：等待中，2：执行中，3：成功，4：失败）',
  `createTime`     TIMESTAMP                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `createUserId`   BIGINT                            NOT NULL COMMENT '创建用户ID',
  `createUserName` VARCHAR(30)                       NOT NULL COMMENT '创建用户昵称',
  `modifyTime`     TIMESTAMP                         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `modifyUserId`   BIGINT                            NOT NULL COMMENT '修改用户ID',
  `modifyUserName` VARCHAR(30)                       NOT NULL COMMENT '修改用户昵称',
  `deleted`        TINYINT(1) UNSIGNED               NOT NULL DEFAULT 0 COMMENT '是否逻辑删除（1、已删除， 0、未删除）',
  KEY (`uid`)
) ENGINE InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '测试案例表';

