import axios from 'axios';
import Result from '../utils/entity/Result';
import Asserts from '../utils/entity/Asserts';

/**
 * 请求 url 定义
 * @author 谢长春 2019-8-1
 */
const USER_URL = Object.freeze({
  login: '/login', // 登录
  logout: '/logout', // 退出
  currentUser: '/user/1/current', // 当前登录用户信息
  updateNickname: '/user/1/nickname' // 修改昵称
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
   * @param id {number} 数据 ID
   * @param uid {string} 用户UUID，缓存和按ID查询时可使用强校验
   * @param username {string} 登录名
   * @param password {string} 登录密码
   * @param nickname {string} 用户昵称
   * @param phone {string} 手机号
   * @param email {string} 邮箱
   * @return {UserVO}
   */
  static of({id = undefined, uid = undefined, username = undefined, password = undefined, nickname = undefined, phone = undefined, email = undefined} = {}) {
    return new UserVO(id, uid, username, password, nickname, phone, email);
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
   */
  constructor(id, uid, username, password, nickname, phone, email) {
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