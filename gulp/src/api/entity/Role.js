import RoleService from '../RoleService';
import Authority from './Authority';

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
   * @param authorities {Array<String>} 权限指令代码集合，同 {@link Authority#code}
   * @param authorityTree {Array<Authority>} 权限指令树
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
     * 权限指令代码集合，同 {@link Authority#code}
     * @type {Array<String>}
     */
    this.authorities = authorities;
    /**
     * 权限指令树
     * @type {Array<Authority>}
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
