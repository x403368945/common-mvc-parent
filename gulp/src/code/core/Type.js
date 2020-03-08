/**
 * 数据类型定义
 */
export default class Type {
  /**
   * 实体: mysql 与 java 数据类型映射
   * @param mysql {string} 数据库数据类型
   * @param java {string} Java数据类型
   * @param javascript {string} JS 数据类型
   */
  constructor(mysql = '', java = '', javascript = '') {
    /**
     * 数据库数据类型
     * @type {string}
     */
    this.mysql = mysql;
    /**
     * Java数据类型
     * @type {string}
     */
    this.java = java;
    /**
     * Java数据类型
     * @type {string}
     */
    this.javascript = javascript;
  }
}
