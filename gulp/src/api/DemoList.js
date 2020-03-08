import axios from 'axios';
import Page from '../utils/entity/Page';
import Result from '../utils/entity/Result';

/**
 * 请求 url 定义
 * @author 谢长春 2019-7-28
 */
const DEMO_LIST_URL = Object.freeze({
  save: '/demo-list/1', // 新增
  update: '/demo-list/1/{id}', // 修改
  deleteById: '/demo-list/1/{id}', // 按 id 删除
  deleteByUid: '/demo-list/1/{id}/{uid}', // 按 id + uid 删除
  markDeleteById: '/demo-list/1/{id}', // 按 id 逻辑删除
  markDeleteByUid: '/demo-list/1/{id}/{uid}', // 按 id + uid 逻辑删除
  markDelete: '/demo-list/1', // 按 id + uid 批量逻辑删除
  findById: '/demo-list/1/{id}', // 按 id 查询单条记录
  findByUid: '/demo-list/1/{id}/{uid}', // 按 id + uid + 时间戳 查询单条记录
  search: '/demo-list/1', // 多条件批量查询，不分页
  page: '/demo-list/1/page/{number}/{size}' // 分页：多条件批量查询
});

/**
 * 后台服务请求：参考案例：实体表操作
 * @author 谢长春 2019-7-28
 */
export class DemoListService {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {DemoListService}
   * @return {DemoListService}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数
   * @param vo {DemoListVO} 参考案例对象
   * @return {DemoListService}
   */
  static of(vo) {
    return new DemoListService(vo);
  }

  /**
   * 静态构造函数
   * @param vo {DemoListVO} 参考案例对象
   */
  constructor(vo) {
    /**
     * 参考案例对象
     * @type {DemoListVO}
     */
    this.vo = vo || new DemoListVO({});
  }

  toString() {
    return JSON.stringify(this)
  }

  /**
   * 新增
   * @return {Promise<Result>}
   */
  async save() {
    const {name, phone} = this.vo;
    return await axios
      .post(DEMO_LIST_URL.save, {
        json: {
          name,
          phone
        }
      })
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
      .put(DEMO_LIST_URL.update.format(id || 0), {json})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id 删除
   * @return {Promise<Result>}
   */
  async deleteById() {
    const {id} = this.vo;
    return await axios
      .delete(DEMO_LIST_URL.deleteById.format(id || 0))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id + uid 删除
   * @return {Promise<Result>}
   */
  async deleteByUid() {
    const {id, uid} = this.vo;
    return await axios
      .delete(DEMO_LIST_URL.deleteByUid.format(id || 0, uid))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id 逻辑删除
   * @return {Promise<Result>}
   */
  async markDeleteById() {
    const {id} = this.vo;
    return await axios
      .patch(DEMO_LIST_URL.markDeleteById.format(id || 0))
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
      .patch(DEMO_LIST_URL.markDeleteByUid.format(id || 0, uid))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id + uid 批量逻辑删除
   * @return {Promise<Result>}
   */
  async markDelete() {
    const {uids} = this.vo;
    return await axios
      .patch(DEMO_LIST_URL.markDelete, {json: uids})
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id + 时间戳 查询单条记录
   * @return {Promise<Result>}
   */
  async findByIdTimestamp() {
    const {id, timestamp} = this.vo;
    return await axios
      .get(DEMO_LIST_URL.findByIdTimestamp.format(id || 0, timestamp))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 按 id + uid + 时间戳 查询单条记录
   * @return {Promise<Result>}
   */
  async findByUidTimestamp() {
    const {id, uid, timestamp} = this.vo;
    return await axios
      .get(DEMO_LIST_URL.findByUidTimestamp.format(id || 0, uid, timestamp))
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 多条件批量查询，不分页
   * @return {Promise<Result>}
   */
  async search() {
    const {id, name, phone, amountRange, insertTimeRange, sorts} = this.vo;
    return await axios
      .get(DEMO_LIST_URL.search, {
        params: {
          json: {
            id: id || undefined,
            name: name || undefined,
            phone: name || undefined,
            amountRange,
            insertTimeRange,
            sorts
          }
        }
      })
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }

  /**
   * 分页：多条件批量查询
   * @return {Promise<Result>}
   */
  async pageable() {
    const {id, name, phone, amountRange, insertTimeRange, sorts, page} = this.vo;
    return await axios
      .get(DEMO_LIST_URL.page.formatObject(page || Page.ofDefault()),
        {
          params: {
            json: {
              id: id || undefined,
              name: name || undefined,
              phone: name || undefined,
              amountRange,
              insertTimeRange,
              sorts
            }
          }
        }
      )
      .then(Result.ofResponse)
      .catch(Result.ofCatch);
  }
}

/**
 * 后台服务请求：参考案例：实体表操作
 * @author 谢长春 2019-7-28
 */
export default class DemoListVO {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {DemoListVO}
   * @return {DemoListVO}
   */
  static self(self) {
    return self;
  }

  /**
   * 将 result 对象中的 data 集合转换为当前对象集合
   * @param data {Array<object>}
   * @return {Array<DemoListVO>}
   */
  static parseList(data) {
    return data.map(new DemoListVO(data || {}));
  }

  /**
   * 构造函数：内部应该列出所有可能的参数，并对参数做说明
   * @param id {number} 数据 ID
   * @param uid {string} 数据UUID，缓存和按ID查询时可使用强校验
   * @param name {string} 名称
   * @param content {string} 内容
   * @param amount {number} 金额
   * @param status {string} 状态，参考 {@link DemoStatus}.*.value
   * @param insertTime {string} 创建时间
   * @param insertUserId {number} 创建用户ID
   * @param insertUserName {string} 创建用户昵称
   * @param updateTime {string} 修改时间
   * @param updateUserId {number} 修改用户ID
   * @param updateUserName {string} 修改用户昵称
   * @param deleted {string} 逻辑删除状态，参考 {@link Bool}.*.value
   * @param timestamp {number} 按 id 查询时可能使用时间戳缓存
   * @param uids {Array<Object>} id + uid批量带参，=> [{id:1,uid:''},{id:1,uid:''}]
   * @param amountRange {NumRange} 金额查询区间
   * @param insertTimeRange {DateRange} 创建时间查询区间
   * @param sorts {Array<OrderBy>} 排序字段集合
   * @param page {Page} 分页对象
   */
  constructor({
                id = undefined,
                uid = undefined,
                name = undefined,
                content = undefined,
                amount = undefined,
                status = undefined,
                insertTime = undefined,
                insertUserId = undefined,
                insertUserName = undefined,
                updateTime = undefined,
                updateUserId = undefined,
                updateUserName = undefined,
                deleted = undefined,
                timestamp = undefined,
                uids = undefined,
                amountRange = undefined,
                insertTimeRange = undefined,
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
     * 内容
     * @type {string}
     */
    this.content = content;
    /**
     * 金额
     * @type {number}
     */
    this.amount = amount;
    /**
     * 状态，参考 {@link DemoStatus}.*.value
     * @type {string}
     */
    this.status = status;
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
     * id + uid批量带参，=> [{id:1,uid:''},{id:1,uid:''}]
     * @type {Array<Object>}
     */
    this.uids = uids;
    /**
     * 金额查询区间
     * @type {NumRange}
     */
    this.amountRange = amountRange;
    /**
     * 创建时间查询区间
     * @type {DateRange}
     */
    this.insertTimeRange = insertTimeRange;
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
   * @return {DemoListService}
   */
  getService() {
    return new DemoListService(this);
  }
}
