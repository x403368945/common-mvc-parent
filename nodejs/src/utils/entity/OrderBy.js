import Asserts from './Asserts';

/**
 * 排序对象
 * @author 谢长春 2019-8-9
 */
export default class OrderBy {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {OrderBy}
   * @return {OrderBy}
   */
  static self(self) {
    return self;
  }

  /**
   * 构造排序对象
   * @param name {string} 排序字段名
   * @param direction {string} 排序方向，默认 ASC
   * @return {OrderBy}
   */
  static of({name, direction}) {
    return new OrderBy(name, direction);
  }

  /**
   * 构造排序对象
   * @param name {string} 排序字段名
   * @return {OrderBy}
   */
  static asc(name) {
    return new OrderBy(name);
  }

  /**
   * 构造排序对象
   * @param name {string} 排序字段名
   * @return {OrderBy}
   */
  static desc(name) {
    return new OrderBy(name, 'DESC');
  }

  /**
   * 构造排序对象
   * @param name {string} 排序字段名
   * @param direction {string} 排序方向，默认 ASC
   */
  constructor(name, direction = 'ASC') {
    Asserts.of().hasFalse(name, () => '【name】排序字段名');
    /**
     * 排序字段名
     * @type {string}
     */
    this.name = name;
    /**
     * 排序方向，默认 ASC
     * @type {string}
     */
    this.direction = direction;
  }

  toString() {
    return JSON.stringify(this)
  }
}
