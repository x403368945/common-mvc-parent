INSERT INTO tab_user(id, uid, domain, username, password, nickname, roles, insertUserId, updateUserId) VALUES
-- 初始化超级管理员账户，密码：admin
(1, replace(uuid(), '-', ''), 'www', 'admin', '$2a$10$VQ.Rj7bc73B.WwU99k7R.eEAwqXBNmvihobk3SZ4m30b9tCR6..h2', '超级管理员', '[1]', 1, 1),
-- user:111111
(2, replace(uuid(), '-', ''), 'www', 'user', '$2a$10$6unbpf74Dc7NEBywaCHl..FzzprMb69gA.Qi09U7ud7vlKHP9PXfu', '普通用户', '[2]', 1, 1),
-- 初始化超级管理员测试账户，密码：admin
(3, replace(uuid(), '-', ''), 'test', 'admin-test', '$2a$10$VQ.Rj7bc73B.WwU99k7R.eEAwqXBNmvihobk3SZ4m30b9tCR6..h2', '超级管理员', '[1]', 1, 1),
-- user:111111
(4, replace(uuid(), '-', ''), 'test', 'user-test', '$2a$10$6unbpf74Dc7NEBywaCHl..FzzprMb69gA.Qi09U7ud7vlKHP9PXfu', '普通用户', '[2]', 1, 1)
;

INSERT INTO tab_role(id, uid, name, authorities, insertUserId, updateUserId) VALUES
(1, replace(uuid(), '-', ''), '超级管理员', '["ROLE_ADMIN"]', 1,1),
(2, replace(uuid(), '-', ''), '普通用户', '["ROLE_USER"]', 1,1)
;
