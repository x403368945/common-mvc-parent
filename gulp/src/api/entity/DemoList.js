import DemoListService from '../DemoListService';

/**
 * 后台服务请求：参考案例：实体表操作
 * @author 谢长春 2019-7-28
 */
export default class DemoList {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {DemoList}
   * @return {DemoList}
   */
  static self(self) {
    return self;
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
