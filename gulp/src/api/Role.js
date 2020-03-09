import axios from 'axios';
import Page from '../utils/entity/Page';
import Result from '../utils/entity/Result';
import AuthorityVO from './Authority';

/**
 * 请求 url 定义
 * @author 谢长春 2019-8-30
 */
const saveURL = '/1/role'; // 新增
const updateURL = '/1/role/{id}'; // 修改
const markDeleteByUidURL = '/1/role/{id}/{uid}'; // 按 id + uid 逻辑删除
const markDeleteURL = '/1/role'; // 按 id + uid 批量逻辑删除
const findByUidURL = '/1/role/{id}/{uid}'; // 按 id + ui 查询单条记录
const pageURL = '/1/role/page/{number}/{size}'; // 分页：多条件批量查询
const optionsURL = '/1/role/options';// 角色下拉列表选项

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
    let vobject = null;
    if (vo) {
      vobject = new RoleVO({...vo});
      Object.keys(vobject).forEach(key => { // 移除空字符串参数，前端组件默认值为空字符串，带到后端查询会有问题
        if (vobject[key] === '') {
          delete vobject[key];
        }
      })
    }
    /**
     * 参考案例对象
     * @type {RoleVO}
     */
    this.vo = vo || new RoleVO({});
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
      .post(saveURL, {
        name,
        authorityTree
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

  /**
   * 角色下拉列表选项
   * @return {Promise<Result>}
   */
  async options() {
    return await axios
      .get(optionsURL)
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
   * 构造函数：内部应该列出所有可能的参数，并对参数做说明
   * @param id {number} 数据 ID
   * @param uid {string} 数据UUID，缓存和按ID查询时可使用强校验
   * @param name {string} 名称
   * @param authorities {Array<String>} 权限指令代码集合，同 {@link AuthorityVO#code}
   * @param authorityTree {Array<AuthorityVO>} 权限指令树
   * @param insertTime {string} 创建时间
   * @param insertUserId {number} 创建用户ID
   * @param insertUserName {string} 创建用户昵称
   * @param updateTime {string} 修改时间
   * @param updateUserId {number} 修改用户ID
   * @param updateUserName {string} 修改用户昵称
   * @param deleted {string} 逻辑删除状态，参考 {@link Bool}.*.value
   * @param timestamp {number} 按 id 查询时可能使用时间戳缓存
   * @param sorts {Array<OrderBy>} 排序字段集合
   * @param page {Page} 分页对象
   * @param markDeleteArray {Array<MarkDelete>} 批量删除
   */
  constructor({
                id = undefined,
                uid = undefined,
                name = undefined,
                authorities = undefined,
                authorityTree = undefined,
                insertTime = undefined,
                insertUserId = undefined,
                insertUserName = undefined,
                updateTime = undefined,
                updateUserId = undefined,
                updateUserName = undefined,
                deleted = undefined,
                timestamp = undefined,
                sorts = undefined,
                page = undefined,
                markDeleteArray = undefined
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
    this.insertTime = insertTime;
    /**
     * 创建用户ID
     * @type {number}
     */
    this.insertUserId = insertUserId;
    /**
     * 创建用户昵称
     * @type {string}
     */
    this.insertUserName = insertUserName;
    /**
     * 修改时间
     * @type {string}
     */
    this.updateTime = updateTime;
    /**
     * 修改用户ID
     * @type {number}
     */
    this.updateUserId = updateUserId;
    /**
     * 修改用户昵称
     * @type {string}
     */
    this.updateUserName = updateUserName;
    /**
     * 逻辑删除状态，参考 {@link Bool}.*.value
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
    /**
     * 批量删除
     * @type {Array<MarkDelete>}
     */
    this.markDeleteArray = markDeleteArray;
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
