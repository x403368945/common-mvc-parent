-- 初始化超级管理员账户，密码：admin
INSERT INTO tab_user(id, uid, username, password, nickname, role, createUserId, modifyUserId)
VALUES (1, replace(uuid(), '-', ''), 'admin', '$2a$10$VQ.Rj7bc73B.WwU99k7R.eEAwqXBNmvihobk3SZ4m30b9tCR6..h2', '超级管理员',0, 1, 1);

-- user:111111
INSERT INTO tab_user(uid, username, password, nickname, role, createUserId, modifyUserId)
VALUES (replace(uuid(), '-', ''), 'user', '$2a$10$6unbpf74Dc7NEBywaCHl..FzzprMb69gA.Qi09U7ud7vlKHP9PXfu', '普通用户',2, 1, 1);

