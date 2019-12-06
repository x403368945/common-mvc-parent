import axios from 'axios';
import Asserts from '../utils/entity/Asserts';
import Result from '../utils/entity/Result';
import Page from "../utils/entity/Page";

/**
 * 请求 url 定义
 * @author 谢长春 2019-8-1
 */
const USER_URL = Object.freeze({
  login: '/login', // 登录
  logout: '/logout', // 退出
  currentUser: '/user/1/current', // 当前登录用户信息
  updateNickname: '/user/1/nickname', // 修改昵称
  save: '/user/1', // 新增用户
  update: '/user/1/{id}', // 修改用户信息
  findByUid: '/user/1/{id}/{uid}', // 查看用户详细信息
  page: '/user/1/page/{number}/{size}', // 分页查看用户信息
  users: '/user/1/users/{roleId}', // 按角色查询用户集合
});

/**
 * 后台服务请求：用户
 * @author 谢长春 2019-7-28
 */
export class UserService {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {UserService}
   * @return {UserService}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @param vo {UserVO} 用户实体对象
   */
  static of(vo) {
    return new UserService(vo);
  }

  /**
   * 构造函数
   * @param vo {UserVO} 用户实体对象
   */
  constructor(vo) {
    Asserts.of().hasFalse(vo, () => 'vo');
    /**
     * 用户实体对象
     * @type {UserVO}
     */
    this.vo = vo;
  }

  toString() {
    return JSON.stringify(this)
  }

  /**
   * 登录
   * @return {Promise<Result>}
   */
  async login() {
    const {username, password} = this.vo;
    return await axios
      .post(USER_URL.login, {json: {username, password}})
      .then(Result.ofResponse)
      .catch(Result.ofCatch)
  }

  /**
   * 退出
   * @return {Promise<Result>}
   */
  async logout() {
    return await axios
      .post(USER_URL.logout)
      .then(Result.ofResponse)
      .catch(Result.ofCatch)
  }

  /**
   * 获取当前登录用户信息
   * @return {Promise<Result>}
   */
  async getCurrentUser() {
    return await axios
      .get(USER_URL.currentUser)
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 修改昵称
   * @return {Promise<Result>}
   */
  async updateNickname() {
    const {nickname} = this.vo;
    return await axios
      .patch(USER_URL.updateNickname, {json: {nickname}})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 新增
   * @return {Promise<Result>}
   */
  async save() {
    const {username, password, nickname, phone, email, roleList} = this.vo;
    return await axios
      .post(USER_URL.save, {json: {username, password, nickname, phone, email, roleList}})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 修改
   * @return {Promise<Result>}
   */
  async update() {
    const {id, ...json} = this.vo;
    return await axios
      .put(USER_URL.update.format(id || 0), {json})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id + uid 查询单条记录
   * @return {Promise<Result>}
   */
  async findByUid() {
    const {id, uid} = this.vo;
    return await axios
      .get(USER_URL.findByUid.format(id || 0, uid))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 分页：多条件批量查询
   * @return {Promise<Result>}
   */
  async pageable() {
    const {id, uid, username, phone, email, createUserId, modifyUserId, expired, locked, nickname, sorts, page} = this.vo;
    return await axios
      .get(USER_URL.page.formatObject(page || Page.ofDefault()),
        {
          params: {
            json: {
              id, uid, username, phone, email, createUserId, modifyUserId, expired, locked, nickname, sorts, page
            }
          }
        }
      )
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按角色查询用户集合
   * @return {Promise<Result>}
   */
  async getUsersFromRole() {
    const {roleId} = this.vo;
    return await axios
      .get(USER_URL.users.format(roleId))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }
}

/**
 * 用户实体对象
 * @author 谢长春 2019-7-28
 */
export default class UserVO {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {UserVO}
   * @return {UserVO}
   */
  static self(self) {
    return self;
  }

  /**
   * 构造参考案例参数
   * @return {UserVO}
   */
  static of(obj) {
    return new UserVO(obj || {});
  }

  /**
   * 构造参考案例参数：构造函数内部应该列出所有可能的参数，并对参数做说明
   * @param id {number} 数据 ID
   * @param uid {string} 用户UUID，缓存和按ID查询时可使用强校验
   * @param username {string} 登录名
   * @param password {string} 登录密码
   * @param nickname {string} 用户昵称
   * @param phone {string} 手机号
   * @param email {string} 邮箱
   * @param roles {Array<number>} 角色 ID 集合
   * @param roleList {Array<RoleVO>} 新增用户时，选择的角色集合，经过验证之后，将角色 ID 保存到 roles
   * @param authorityList {Array<string>} 角色对应的权限指令集合
   * @param roleId {number} 角色 id {@link RoleVO#id}，按角色查询用户列表时使用该字段
   */
  constructor({
                id = undefined,
                uid = undefined,
                username = undefined,
                password = undefined,
                nickname = undefined,
                phone = undefined,
                email = undefined,
                roles = undefined,
                roleList = undefined,
                authorityList = undefined,
                roleId = undefined
              } = {}) {
    /**
     * 数据 ID
     * @type {number}
     */
    this.id = id;
    /**
     * 用户UUID，缓存和按ID查询时可使用强校验
     * @type {string}
     */
    this.uid = uid;
    /**
     * 登录名
     * @type {string}
     */
    this.username = username;
    /**
     * 登录密码
     * @type {string}
     */
    this.password = password;
    /**
     * 用户昵称
     * @type {string}
     */
    this.nickname = nickname;
    /**
     * 手机号
     * @type {string}
     */
    this.phone = phone;
    /**
     * 邮箱
     * @type {string}
     */
    this.email = email;
    /**
     * 角色 ID 集合
     * @type {Array<number>}
     */
    this.roles = roles;
    /**
     * 新增用户时，选择的角色集合，经过验证之后，将角色 ID 保存到 roles
     * @type {Array<RoleVO>}
     */
    this.roleList = roleList;
    /**
     * 角色对应的权限指令集合
     * @type {Array<string>}
     */
    this.authorityList = authorityList;
    /**
     * 角色 id {@link RoleVO#id}
     * @type {number}
     */
    this.roleId = roleId;
  }

  toString() {
    return JSON.stringify(this)
  }

  /**
   * 获取 api 服务对象
   * @return {UserService}
   */
  getService() {
    return new UserService(this);
  }
}