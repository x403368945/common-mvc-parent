-- mysql + mongo 镜像
-- DROP TABLE IF EXISTS tab_demo_jpa_mongo;
-- CREATE TABLE tab_demo_jpa_mongo (
--   `id`             BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '数据ID，主键自增',
--   `uid`            CHAR(32)                          NOT NULL COMMENT '数据UUID，缓存和按ID查询时可使用强校验',
--   `name`           VARCHAR(50)                       NOT NULL COMMENT '姓名',
--   `phone`          VARCHAR(50)                       NOT NULL DEFAULT '' COMMENT '手机',
--   `age`            TINYINT(3) UNSIGNED               NULL COMMENT '年龄',
--   `insertTime`     TIMESTAMP                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
--   `insertUserId`   BIGINT                            NOT NULL COMMENT '创建用户ID',
--   `updateTime`     TIMESTAMP(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3)  ON UPDATE CURRENT_TIMESTAMP(3)  COMMENT '修改时间',
--   `updateUserId`   BIGINT                            NOT NULL COMMENT '修改用户ID',
--   `deleted`        TINYINT(1) UNSIGNED               NOT NULL DEFAULT 0 COMMENT '是否逻辑删除（1、已删除， 0、未删除）'
-- ) ENGINE InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '测试案例表：将mysql数据与mongodb同步';

-- 测试表
DROP TABLE IF EXISTS tab_demo_list;
CREATE TABLE tab_demo_list (
  `id`             BIGINT UNSIGNED     NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '数据ID，主键自增',
  `uid`            CHAR(32)            NOT NULL COMMENT '数据UUID，缓存和按ID查询时可使用强校验',
  `name`           VARCHAR(50)         NOT NULL DEFAULT '' COMMENT '名称',
  `content`        TEXT                NULL COMMENT '内容',
  `amount`         DECIMAL(18, 2)      NULL COMMENT '金额',
  `status`         TINYINT(2) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态（0：无效，1：等待中，2：执行中，3：成功，4：失败）',
  `insertTime`     TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `insertUserId`   BIGINT              NOT NULL COMMENT '创建用户ID',
  `insertUserName` VARCHAR(30)         NOT NULL COMMENT '创建用户昵称',
  `updateTime`     TIMESTAMP(3)        NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `updateUserId`   BIGINT              NOT NULL COMMENT '修改用户ID',
  `updateUserName` VARCHAR(30)         NOT NULL COMMENT '修改用户昵称',
  `deleted`        TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否逻辑删除（1、已删除， 0、未删除）',
  KEY (`uid`)
)
  ENGINE InnoDB
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci COMMENT '测试案例表';

-- 测试自定义 Convert 表
DROP TABLE IF EXISTS tab_convert;
CREATE TABLE tab_convert (
  `id`           BIGINT UNSIGNED     NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '数据ID，主键自增',
  `uid`          CHAR(32)            NOT NULL COMMENT '数据UUID，缓存和按ID查询时可使用强校验',
  `ids`          JSON                NULL COMMENT '{@link List<Long>}',
  `images`       JSON                NULL COMMENT '{@link List<String>}',
  `codes`        JSON                NULL COMMENT '{@link List<com.support.mvc.enums.Code>}',
  `items`        JSON                NULL COMMENT '{@link List<com.support.mvc.entity.base.Item>}',
  `item`         JSON                NULL COMMENT '{@link com.support.mvc.entity.base.Item}',
  `insertTime`   TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `insertUserId` BIGINT              NOT NULL COMMENT '创建用户ID',
  `updateTime`   TIMESTAMP(3)        NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `updateUserId` BIGINT              NOT NULL COMMENT '修改用户ID',
  `deleted`      TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否逻辑删除（1、已删除， 0、未删除）',
  KEY (`uid`)
)
  ENGINE InnoDB
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci COMMENT '测试自定义 Convert 表';

