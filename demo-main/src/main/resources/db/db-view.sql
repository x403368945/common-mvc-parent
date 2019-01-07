-- 查询所有视图
SHOW TABLE STATUS WHERE Comment = 'view';

/*
CREATE OR REPLACE VIEW view_vip AS
  SELECT
    u.id, -- 用户ID
    u.email, -- 用户邮箱
    u.nickname, -- 用户昵称
    u.role, -- ROLE_ADMIN【0】:超级管理员,ROLE_SYS_ADMIN【1】:系统管理员,ROLE_USER【2】:普通用户,ROLE_VIP【3】:VIP用户
    u.phone -- 手机号
  FROM tab_user u
  WHERE u.role = 3
;

SELECT * FROM view_vip;
*/
