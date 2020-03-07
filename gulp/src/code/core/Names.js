/**
 * 基于表名生成各种命名
 */
export default class Names {
  /**
   * 构造表名各种规则
   * @param tableName {string}
   */
  constructor(tableName) {
    /**
     * tab_demo_list => tab_demo_list
     * @type {string}
     */
    this.tab_name = tableName;
    /**
     * tab_demo_list => tabDemoList
     * @type {string}
     */
    this.TabName = tableName.split('_').map(v => v.replace(/^\S/, m => m.toUpperCase())).join('');
    /**
     * tab_demo_list => tabDemoList
     * @type {string}
     */
    this.tabName = this.TabName.replace(/^\S/, m => m.toLowerCase());
    /**
     * tab_demo_list => demo-list
     * @type {string}
     */
    this.java_name = tableName.replace(/^[a-zA-Z]+_/, '').replace(/_/g, '-');
    /**
     * tab_demo_list => DemoList
     * @type {string}
     */
    this.JavaName = this.java_name.split('-').map(v => v.replace(/^\S/, m => m.toUpperCase())).join('');
    /**
     * tab_demo_list => demoList
     * @type {string}
     */
    this.javaName = this.JavaName.replace(/^\S/, m => m.toLowerCase());
    /**
     * tab_demo_list => demolist
     * @type {string}
     */
    this.javaname = this.JavaName.toLowerCase();
  }

  /**
   * 转换为对象
   * @return {Object}
   */
  toObject() {
    return JSON.parse(JSON.stringify(this));
  }

  toString() {
    return JSON.stringify(this);
  }
}
