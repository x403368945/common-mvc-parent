/**
 * 测试：后台服务请求：用户
 * @author 谢长春 2019-7-28
 */
import axios from 'axios';
import sample from 'lodash/sample';
import sampleSize from 'lodash/sampleSize';
import User from '../../src/api/entity/User';
import UserService from '../../src/api/UserService';
import RoleService from '../../src/api/RoleService';
import MarkDelete from '../../src/utils/entity/MarkDelete';
import CommonService from "../../src/api/CommonService";
import fs from "fs";

/**
 * 当前登录用户
 * @type {User}
 */
export const sessionUser = {};

export default class UserServiceTest {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {UserServiceTest}
   * @return {UserServiceTest}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @return {UserServiceTest}
   */
  static of() {
    return new UserServiceTest();
  }

  /**
   * 获取当前登录用户信息
   * @return {User}
   */
  getSessionUser() {
    return sessionUser;
  }

  /**
   * @return {Promise<UserServiceTest>}
   */
  async login() {
    console.log('> 登录：管理员(admin-test:admin) ----------------------------------------------------------------------------------------------------');
    const result = (await new User({
      username: 'admin-test',
      password: 'admin'
    }).getService().login())
      .print()
      .assertData();
    result.dataFirst(user => {
      Object.keys(sessionUser).forEach(key => {
        delete sessionUser[key];
      });
      Object.assign(sessionUser, user);
    });
    return this;
  }

  /**
   * @return {Promise<UserServiceTest>}
   */
  async loginAdminBasic() {
    console.log('> 登录[basic]：用户(admin-test:admin) ----------------------------------------------------------------------------------------------------');
    axios.defaults.auth = {
      username: 'admin-test',
      password: 'admin'
    };
    (await new UserService().getCurrentUser()).dataFirst(user => {
      Object.keys(sessionUser).forEach(key => {
        delete sessionUser[key];
      });
      Object.assign(sessionUser, user);
    });
    return this;
  }

  /**
   * @return {Promise<UserServiceTest>}
   */
  async loginUserBasic() {
    console.log('> 登录[basic]：用户(user-test:111111) ----------------------------------------------------------------------------------------------------');
    axios.defaults.auth = {
      username: 'user-test',
      password: '111111'
    };
    (await new UserService().getCurrentUser()).dataFirst(user => {
      Object.keys(sessionUser).forEach(key => {
        delete sessionUser[key];
      });
      Object.assign(sessionUser, user);
    });
    return this;
  }

  /**
   * @return {Promise<UserServiceTest>}
   */
  async logout() {
    console.log('> 退出 ----------------------------------------------------------------------------------------------------');
    (await new User().getService().logout())
      .print()
      .assertCode();
    return this;
  }

  /**
   * @return {Promise<UserServiceTest>}
   */
  async getCurrentUser() {
    console.log('> 获取当前登录用户信息 ----------------------------------------------------------------------------------------------------');
    (await new User().getService().getCurrentUser())
      .print()
      .assertData();
    return this;
  }

  /**
   *
   * @return {Promise<UserServiceTest>}
   */
  async updateNickname() {
    console.log('> 修改昵称 ----------------------------------------------------------------------------------------------------');
    (await new User({
      nickname: `管理员${Math.random()}`
    }).getService().updateNickname())
      .print()
      .assertCode();
    return this;
  }

  /**
   *
   * @return {Promise<UserServiceTest>}
   */
  async save() {
    console.log('> 新增用户 ----------------------------------------------------------------------------------------------------');
    const {data: roles} = await new RoleService().options();
    for (let i = 0; i < 2; i++) {
      (await new User({
        username: `${new Date().getTime()}`,
        password: '111111',
        nickname: '随机单个角色',
        domain: 'test',
        roleList: [sample(roles)]
      }).getService().save())
        .print()
        .assertCode();
      (await new User({
        username: `${new Date().getTime()}`,
        password: '111111',
        nickname: '随机多个角色',
        domain: 'test',
        roleList: sampleSize(roles, parseInt(`${Math.random()}`.slice(-1)) + 1)
      }).getService().save())
        .print()
        .assertCode();
    }

    const {data: [avatar]} = (await new CommonService().uploadUser(fs.createReadStream('./favicon.ico')));
    (await new User({
      username: `${new Date().getTime()}`,
      password: '111111',
      nickname: '随机单个角色',
      domain: 'test',
      roleList: [sample(roles)],
      avatar
    }).getService().save())
      .print()
      .assertCode();
    return this;
  }

  /**
   *
   * @return {Promise<UserServiceTest>}
   */
  async update() {
    console.log('> 修改用户 ----------------------------------------------------------------------------------------------------');
    const {data: roles} = await new RoleService().options();
    const {data: users} = await new User({
      domain: 'test',
      insertUserId: sessionUser.id
    }).getService().pageable();
    const {data: [avatar]} = (await new CommonService().uploadUser(fs.createReadStream('./favicon.ico')));
    (await new User(Object.assign(sample(users), {
      nickname: `编辑用户-${new Date().formatDate()}`,
      roleList: sampleSize(roles, parseInt(`${Math.random()}`.slice(-1)) + 1),
      avatar
    })).getService().update())
      .print()
      .assertCode();
    return this;
  }

  /**
   * @return {Promise<UserServiceTest>}
   */
  async markDeleteByUId() {
    console.log('> 按 id + uid 逻辑删除：在查询结果集中随机选取一条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new User({
      insertUserId: sessionUser.id
    }).getService().pageable()).assertData();
    const {id, uid} = sample(data);
    (await new User({
      id,
      uid
    }).getService().markDeleteByUid())
      .print()
      .assertCode();
    return this;
  }

  /**
   * @return {Promise<UserServiceTest>}
   */
  async markDelete() {
    console.log('> 按 id + uid 批量逻辑删除：在查询结果集中随机选取 2 条数据 ----------------------------------------------------------------------------------------------------');
    const {data} = (await new User({
      insertUserId: sessionUser.id
    }).getService().pageable())
      .assertData();
    const arrs = sampleSize(data, 2);
    (await new User({
      markDeleteArray: arrs.map(row => new MarkDelete(row))
    }).getService().markDelete())
      .print()
      .assertCode();
    return this;
  }

  /**
   *
   * @return {Promise<UserServiceTest>}
   */
  async findByUid() {
    console.log('> 查看用户详细信息 ----------------------------------------------------------------------------------------------------');
    const {data: users} = await new User().getService().pageable();
    const {id, uid} = sample(users);
    (await new User({
      id,
      uid
    }).getService().findByUid())
      .print()
      .assertCode()
      .assertData();
    return this;
  }

  /**
   *
   * @return {Promise<UserServiceTest>}
   */
  async pageable() {
    console.log('> 分页查询用户 ----------------------------------------------------------------------------------------------------');
    (await new User().getService().pageable())
      .print()
      .assertCode()
      .assertData();
    return this;
  }

  // /**
  //  *
  //  * @return {Promise<UserServiceTest>}
  //  */
  // async getUsersFromRole() {
  //   console.log('> 按角色查询用户集合 ----------------------------------------------------------------------------------------------------');
  //   (await new User({roleId: 1}).getService().getUsersFromRole()).print().assertCode().assertData();
  //   return this;
  // }

  /**
   *
   * @return {UserServiceTest}
   */
  filename() {
    console.log(__filename);
    console.log('');
    return this;
  }

  /**
   *
   * @return {UserServiceTest}
   */
  newline() {
    console.log('');
    return this;
  }

  /**
   * 测试全部
   * @return {Promise<void>}
   */
  async testAll() {
    const moduleName = '用户';
    console.info(`${moduleName}：start ${'*'.repeat(200)}`);
    await Promise.resolve(UserServiceTest.of())
      .then(service => service.filename())
      // 开始
      .then(service => service.login()).then(s => s.newline())
      .then(service => service.loginAdminBasic())
      .then(service => service.getCurrentUser()).then(s => s.newline())
      .then(service => service.logout()).then(s => s.newline())
      .then(service => service.loginUserBasic())
      .then(service => service.getCurrentUser()).then(s => s.newline())
      .then(service => service.updateNickname()).then(s => s.newline())
      .then(service => service.logout()).then(s => s.newline())
      .then(service => service.loginAdminBasic())
      .then(service => service.save()).then(s => s.newline())
      .then(service => service.pageable()).then(s => s.newline())
      .then(service => service.update()).then(s => s.newline())
      .then(service => service.markDeleteByUId()).then(s => s.newline())
      .then(service => service.markDelete()).then(s => s.newline())
      .then(service => service.findByUid()).then(s => s.newline())
      // .then(service => service.getUsersFromRole()).then(s => s.newline())
      // 结束
      .then(() => console.info(`${moduleName}：end ${'*'.repeat(200)}\n\n\n\n\n`))
      .catch((e) => {
        console.info(`${moduleName}：异常：${e.message}`);
        console.error(e)
      })
    ;
    return null;
  }
}
