import axios from 'axios';
import Result from '../utils/entity/Result';
import Page from '../utils/entity/Page';
import User from './entity/User';
import pickBy from 'lodash/pickBy';
import isArray from 'lodash/isArray';
import isObject from 'lodash/isObject';

const loginURL = '/login'; // 登录
const logoutURL = '/logout'; // 退出
const currentUserURL = '/1/user/current'; // 当前登录用户信息
const updateNicknameURL = '/1/user/nickname'; // 修改昵称
const saveURL = '/1/user'; // 新增用户
const updateURL = '/1/user/{id}'; // 修改用户信息
const markDeleteByUidURL = '/1/user/{id}/{uid}'; // 按 id + uid 逻辑删除
const markDeleteURL = '/1/user'; // 按 id + uid 批量逻辑删除
const findByUidURL = '/1/user/{id}/{uid}'; // 查看用户详细信息
const pageURL = '/1/user/page/{number}/{size}'; // 分页查看用户信息
// users: '/1/user/users/{roleId}' // 按角色查询用户集合

/**
 * 后台服务请求：用户
 * @author 谢长春 2019-7-28
 */
export default class UserService {
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
   * @param vo {User} 用户实体对象
   */
  static of(vo) {
    return new UserService(vo);
  }

  /**
   * 构造函数
   * @param vo {User} 用户实体对象
   */
  constructor(vo = {}) {
    /**
     * 用户实体对象
     * @type {User}
     */
    this.vo = new User(pickBy(JSON.parse(JSON.stringify(vo)), value => value !== ''));
    Object.keys(this.vo).forEach(key => { // 移除空字符串参数，前端组件默认值为空字符串，带到后端会有问题
      if (isArray(this.vo[key])) {
        this.vo[key] = this.vo[key].filter(item => item !== '');
        this.vo[key].forEach(item => {
          if (!isArray(item) && isObject(item)) {
            pickBy(item, value => value !== '');
          }
        })
      }
    });
  }

  /**
   * 登录
   * @return {Promise<Result>}
   */
  async login() {
    const {username, password} = this.vo;
    return await axios
      .post(loginURL, {
        username,
        password
      })
      .then(Result.ofResponse)
      .catch(Result.ofCatch)
  }

  /**
   * 退出
   * @return {Promise<Result>}
   */
  async logout() {
    return await axios
      .post(logoutURL)
      .then(Result.ofResponse)
      .catch(Result.ofCatch)
  }

  /**
   * 获取当前登录用户信息
   * @return {Promise<Result>}
   */
  async getCurrentUser() {
    return await axios
      .get(currentUserURL)
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
      .patch(updateNicknameURL, {nickname})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 新增
   * @return {Promise<Result>}
   */
  async save() {
    const {username, password, nickname, phone, email, roleList, domain, avatar} = this.vo;
    return await axios
      .post(saveURL, {
        username,
        password,
        nickname,
        phone,
        email,
        roleList,
        domain,
        avatar
      })
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 修改
   * @return {Promise<Result>}
   */
  async update() {
    const {id, ...body} = this.vo;
    return await axios
      .put(updateURL.format(id || 0), body)
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id + uid 逻辑删除
   * @return {Promise<Result>}
   */
  async markDeleteByUid() {
    const {id, uid} = this.vo;
    return await axios
      .patch(markDeleteByUidURL.format(id || 0, uid))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id + uid 批量逻辑删除
   * @return {Promise<Result>}
   */
  async markDelete() {
    const {markDeleteArray} = this.vo;
    return await axios
      .patch(markDeleteURL, markDeleteArray)
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
      .get(findByUidURL.format(id || 0, uid))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 分页：多条件批量查询
   * @return {Promise<Result>}
   */
  async pageable() {
    const {page, markDeleteArray, ...params} = this.vo;
    return await axios
      .get(pageURL.formatObject(page || Page.ofDefault()), {params})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  // /**
  //  * 按角色查询用户集合
  //  * @return {Promise<Result>}
  //  */
  // async getUsersFromRole() {
  //   const {roleId} = this.vo;
  //   return await axios
  //     .get(usersURL.format(roleId))
  //     .then(Result.ofResponse)
  //     .catch(Result.ofCatch);
  // }
}
