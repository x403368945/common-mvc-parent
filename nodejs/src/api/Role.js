import axios from 'axios';
import Page from '../utils/entity/Page';
import Result from '../utils/entity/Result';
import Asserts from '../utils/entity/Asserts';

/**
 * 请求 url 定义
 * @author 谢长春 2019-8-30
 */
const ROLE_URL = Object.freeze({
  save: '/role/1', // 新增
  update: '/role/1/{id}', // 修改
  markDeleteByUid: '/role/1/{id}/{uid}', // 按 id + uid 逻辑删除
  findByUid: '/role/1/{id}/{uid}', // 按 id + uid + 时间戳 查询单条记录
  page: '/role/1/page/{number}/{size}', // 分页：多条件批量查询
  options: '/role/1/options' // 角色下拉列表选项
});

/**
 * 后台服务请求：角色
 * @author 谢长春 2019-7-28
 */
export class RoleService {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {RoleService}
   * @return {RoleService}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @param vo {RoleVO} 参考案例对象
   * @return {RoleService}
   */
  static of(vo) {
    return new RoleService(vo);
  }

  /**
   * 静态构造函数
   * @param vo {RoleVO} 参考案例对象
   */
  constructor(vo) {
    Asserts.of().hasFalse(vo, () => 'vo');
    /**
     * 参考案例对象
     * @type {RoleVO}
     */
    this.vo = vo;
  }

  toString() {
    return JSON.stringify(this)
  }

  /**
   * 新增
   * @return {Promise<Result>}
   */
  async save() {
    const {name, authorityTree} = this.vo;
    return await axios
      .post(ROLE_URL.save, {json: {name, authorityTree}})
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
      .put(ROLE_URL.update.format(id || 0), {json})
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
      .patch(ROLE_URL.markDeleteByUid.format(id || 0, uid))
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
      .get(ROLE_URL.findByUid.format(id || 0, uid))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 分页：多条件批量查询
   * @return {Promise<Result>}
   */
  async pageable() {
    const {id, name, phone, amountRange, createTimeRange, sorts, page} = this.vo;
    return await axios
      .get(ROLE_URL.page.formatObject(page || Page.ofDefault()),
        {
          params: {
            json: {
              id: id || undefined,
              name: name || undefined,
              phone: name || undefined,
              amountRange,
              createTimeRange,
              sorts
            }
          }
        }
      )
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 角色下拉列表选项
   * @return {Promise<Result>}
   */
  async options() {
    return await axios
      .get(ROLE_URL.options)
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }
}

/**
 * 后台服务请求：角色
 * @author 谢长春 2019-7-28
 */
export default class RoleVO {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {RoleVO}
   * @return {RoleVO}
   */
  static self(self) {
    return self;
  }

  /**
   * 将 result 对象中的 data 集合转换为当前对象集合
   * @param data {Array<object>}
   * @return {Array<RoleVO>}
   */
  static parseList(data) {
    return data.map(new RoleVO(data || {}));
  }

  /**
   * 构造函数：内部应该列出所有可能的参数，并对参数做说明
   * @param id {number} 数据 ID
   * @param uid {string} 数据UUID，缓存和按ID查询时可使用强校验
   * @param name {string} 名称
   * @param authorities {Array<String>} 权限指令代码集合，同 {@link AuthorityVO#code}
   * @param authorityTree {Array<AuthorityVO>} 权限指令树
   * @param createTime {string} 创建时间
   * @param createUserId {number} 创建用户ID
   * @param createUserName {string} 创建用户昵称
   * @param modifyTime {string} 修改时间
   * @param modifyUserId {number} 修改用户ID
   * @param modifyUserName {string} 修改用户昵称
   * @param deleted {string} 逻辑删除状态，参考 {@link Radio}.*.value
   * @param timestamp {number} 按 id 查询时可能使用时间戳缓存
   * @param sorts {Array<OrderBy>} 排序字段集合
   * @param page {Page} 分页对象
   */
  constructor({
                id = undefined,
                uid = undefined,
                name = undefined,
                authorities = undefined,
                authorityTree = undefined,
                createTime = undefined,
                createUserId = undefined,
                createUserName = undefined,
                modifyTime = undefined,
                modifyUserId = undefined,
                modifyUserName = undefined,
                deleted = undefined,
                timestamp = undefined,
                sorts = undefined,
                page = undefined
              } = {}) {
    /**
     * 数据 ID
     * @type {number}
     */
    this.id = id;
    /**
     * 数据UUID，缓存和按ID查询时可使用强校验
     * @type {string}
     */
    this.uid = uid;
    /**
     * 名称
     * @type {string}
     */
    this.name = name;
    /**
     * 权限指令代码集合，同 {@link AuthorityVO#code}
     * @type {Array<String>}
     */
    this.authorities = authorities;
    /**
     * 权限指令树
     * @type {Array<AuthorityVO>}
     */
    this.authorityTree = authorityTree;
    /**
     * 创建时间
     * @type {string}
     */
    this.createTime = createTime;
    /**
     * 创建用户ID
     * @type {number}
     */
    this.createUserId = createUserId;
    /**
     * 创建用户昵称
     * @type {string}
     */
    this.createUserName = createUserName;
    /**
     * 修改时间
     * @type {string}
     */
    this.modifyTime = modifyTime;
    /**
     * 修改用户ID
     * @type {number}
     */
    this.modifyUserId = modifyUserId;
    /**
     * 修改用户昵称
     * @type {string}
     */
    this.modifyUserName = modifyUserName;
    /**
     * 逻辑删除状态，参考 {@link Radio}.*.value
     * @type {string}
     */
    this.deleted = deleted;
    /**
     * 按 id 查询时可能使用时间戳缓存
     * @type {number}
     */
    this.timestamp = timestamp;
    /**
     * 排序字段集合
     * @type {Array<OrderBy>}
     */
    this.sorts = sorts;
    /**
     * 分页对象
     * @type {Page}
     */
    this.page = page;
  }

  toString() {
    return JSON.stringify(this)
  }

  /**
   * 获取 api 服务对象
   * @return {RoleService}
   */
  getService() {
    return new RoleService(this);
  }
}
