/**
 * 批量逻辑删除
 * @author 谢长春 2019-8-9
 */
export default class MarkDelete {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {MarkDelete}
   * @return {MarkDelete}
   */
  static self(self) {
    return self;
  }

  /**
   * 构造批量逻辑删除对象
   * @return {MarkDelete}
   */
  static of(vo) {
    return new MarkDelete(vo || {});
  }

  /**
   * 构造批量逻辑删除对象
   * @param id {string|number} 数据 id
   * @param uid {string} 数据 uid
   * @param timestamp {number} 最后一次更新数据时间戳
   */
  constructor({id, uid, timestamp}) {
    /**
     * 数据 id
     * @type {string|number}
     */
    this.id = id;
    /**
     * 数据 uid
     * @type {string}
     */
    this.uid = uid;
    /**
     * 最后一次更新数据时间戳
     * @type {number}
     */
    this.timestamp = timestamp;
  }

  toString() {
    return JSON.stringify(this)
  }
}
