import OpenDemoService from '../OpenDemoService';

/**
 * 实体：参考案例：用于调试后端服务是否可用，以及基本传参格式是否正确，没有数据入库
 * @author 谢长春 2019-7-28
 */
export default class OpenDemo {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {OpenDemo}
   * @return {OpenDemo}
   */
  static self(self) {
    return self;
  }

  /**
   * 构造参考案例参数：构造函数内部应该列出所有可能的参数，并对参数做说明
   * @param id {number} 数据 ID
   * @param name {string} 姓名
   * @param phone {string} 手机号
   * @param ids {Array<number>} 批量操作 ids
   * @param page {Page} 分页对象
   */
  constructor({id = undefined, name = undefined, phone = undefined, ids = undefined, page = undefined} = {}) {
    /**
     * 数据 ID
     * @type {number}
     */
    this.id = id;
    /**
     * 姓名
     * @type {string}
     */
    this.name = name;
    /**
     * 手机号
     * @type {string}
     */
    this.phone = phone;
    /**
     * 批量操作 ids
     * @type {Array<number>}
     */
    this.ids = ids;
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
   * @return {OpenDemoService}
   */
  getService() {
    return new OpenDemoService(this);
  }
}
