/**
 * 数据类型定义
 */
export default class Type {
  /**
   * 实体: mysql 与 java 数据类型映射
   * @param name {string} 数据库数据类型
   * @param value {string} Java数据类型
   */
  constructor(name = '', value = '') {
    /**
     * 数据库数据类型
     * @type {string}
     */
    this.name = name;
    /**
     * Java数据类型
     * @type {string}
     */
    this.value = value;
  }
}
