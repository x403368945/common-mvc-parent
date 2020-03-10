/**
 * 测试：后台服务请求：用户
 * @author 谢长春 2019-7-28
 */
import UserVO, {UserService} from '../../src/api/User';
import axios from 'axios';
import sample from 'lodash/sample';
import sampleSize from 'lodash/sampleSize';
import {RoleService} from '../../src/api/Role';

/**
 * 当前登录用户
 * @type {UserVO}
 */
export const sessionUser = {};

export default class UserTest {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {UserTest}
   * @return {UserTest}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @return {UserTest}
   */
  static of() {
    return new UserTest();
  }

  /**
   * 获取当前登录用户信息
   * @return {UserVO}
   */
  getSessionUser() {
    return sessionUser;
  }

  /**
   * @return {Promise<UserTest>}
   */
  async login() {
    console.log('> 登录：管理员(admin-test:admin) ----------------------------------------------------------------------------------------------------');
    const result = (await UserVO.of({
      username: 'admin-test',
      password: 'admin'
    }).getService().login()).print().assertData();
    result.dataFirst(user => {
      Object.keys(sessionUser).forEach(key => {
        delete sessionUser[key];
      });
      Object.assign(sessionUser, user);
    });
    return this;
  }

  /**
   * @return {Promise<UserTest>}
   */
  async loginAdminBasic() {
    console.log('> 登录[basic]）：用户(admin-test:admin) ----------------------------------------------------------------------------------------------------');
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
   * @return {Promise<UserTest>}
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
   * @return {Promise<UserTest>}
   */
  async logout() {
    console.log('> 退出 ----------------------------------------------------------------------------------------------------');
    (await UserVO.of().getService().logout()).print().assertVersion().assertCode();
    return this;
  }

  /**
   * @return {Promise<UserTest>}
   */
  async getCurrentUser() {
    console.log('> 获取当前登录用户信息 ----------------------------------------------------------------------------------------------------');
    (await UserVO.of().getService().getCurrentUser()).print().assertVersion().assertData();
    return this;
  }

  /**
   *
   * @return {Promise<UserTest>}
   */
  async updateNickname() {
    console.log('> 修改昵称 ----------------------------------------------------------------------------------------------------');
    (await UserVO.of({nickname: `管理员${Math.random()}`}).getService().updateNickname()).print().assertVersion().assertCode();
    return this;
  }

  /**
   *
   * @return {Promise<UserTest>}
   */
  async save() {
    console.log('> 新增用户 ----------------------------------------------------------------------------------------------------');
    const {data: roles} = await new RoleService().options();
    (await UserVO.of({
      username: `${new Date().getTime()}`,
      password: '111111',
      nickname: '随机单个角色',
      domain: 'test',
      roleList: [sample(roles)]
    }).getService().save()).print().assertVersion().assertCode();
    (await UserVO.of({
      username: `${new Date().getTime()}`,
      password: '111111',
      nickname: '随机多个角色',
      domain: 'test',
      roleList: sampleSize(roles, parseInt(`${Math.random()}`.slice(-1)) + 1)
    }).getService().save()).print().assertVersion().assertCode();
    return this;
  }

  /**
   *
   * @return {Promise<UserTest>}
   */
  async update() {
    console.log('> 修改用户 ----------------------------------------------------------------------------------------------------');
    const {data: roles} = await new RoleService().options();
    const {data: users} = await new UserVO({
      domain: 'test'
    }).getService().pageable();
    const user = sample(users.filter(row => ![1, 2, 3, 4].includes(row.id)));
    (await UserVO.of(Object.assign(user, {
      nickname: `编辑用户-${new Date().formatDate()}`,
      roleList: sampleSize(roles, parseInt(`${Math.random()}`.slice(-1)) + 1)
    })).getService().update()).print().assertVersion().assertCode();
    return this;
  }

  /**
   *
   * @return {Promise<UserTest>}
   */
  async findByUid() {
    console.log('> 查看用户详细信息 ----------------------------------------------------------------------------------------------------');
    const {data: users} = await new UserVO().getService().pageable();
    const {id, uid} = sample(users);
    (await UserVO.of({
      id,
      uid
    }).getService().findByUid()).print().assertVersion().assertCode().assertData();
    return this;
  }

  /**
   *
   * @return {Promise<UserTest>}
   */
  async pageable() {
    console.log('> 分页查询用户 ----------------------------------------------------------------------------------------------------');
    (await UserVO.of().getService().pageable()).print().assertVersion().assertCode().assertData();
    return this;
  }

  // /**
  //  *
  //  * @return {Promise<UserTest>}
  //  */
  // async getUsersFromRole() {
  //   console.log('> 按角色查询用户集合 ----------------------------------------------------------------------------------------------------');
  //   (await UserVO.of({roleId: 1}).getService().getUsersFromRole()).print().assertVersion().assertCode().assertData();
  //   return this;
  // }

  /**
   *
   * @return {UserTest}
   */
  filename() {
    console.log(__filename);
    console.log('');
    return this;
  }

  /**
   *
   * @return {UserTest}
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
    await Promise.resolve(UserTest.of())
      .then(service => service.filename()).then(s => s.newline())
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
