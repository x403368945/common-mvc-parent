import fs from 'fs';
import Paths from '../../utils/entity/Paths';
import Names from './Names';
import Column from './Column';
import DataType from './DataType';
import BaseAdapter from '../entity/adapter/BaseAdapter';

/**
 * 数据库表信息
 */
export default class Table {
  /**
   * 表信息，通过 SHOW TABLE STATUS FROM #{databaseName} 获取到的参数
   * @param Name {string} 表名
   * @param Engine {string} 存储引擎
   * @param Collation {string} 字符集
   * @param Comment {string} 表说明
   */
  constructor({Name, Engine, Collation, Comment}) {
    /**
     * 表名
     * @type {string}
     */
    this.name = Name;
    /**
     * 存储引擎
     * @return {string}
     */
    this.engine = Engine;
    /**
     * 默认字符集
     * @return {string}
     */
    this.collation = Collation;
    /**
     * 表说明
     * @return {string}
     */
    this.comment = Comment.replace(/\s/g, ' ').replace(/"/g, '\'');
    /**
     * 基于表名生成各种命名
     * @type {Names}
     */
    this.names = new Names(this.name);
    /**
     * 当前日期
     * @type {string}
     */
    this.date = new Date().formatDate();
    this.setAdapters();
  }

  /**
   * 设置基础包名
   * @param pkg {string} 包名
   * @result {Table}
   */
  setPkg(pkg) {
    /**
     * 基础包名
     * @type {string}
     */
    this.pkg = pkg;
    return this;
  }

  /**
   * 设置列属性
   * @param columns {Array<Object>} 列集合
   * @result {Table}
   */
  setColumns(columns = []) {
    /**
     * 列属性
     * @return {Array<Column>}
     */
    this.columns = columns.map((obj, index) => {
      obj.index = index;
      return new Column(obj);
    });
    /**
     * 主键数据类型
     * @type {string}
     */
    this.idType = (() => {
      // 目前只支持 Long 、 String 类型作为ID
      const dataType = (this.columns.find(({name}) => name === 'id') || {dataType: {}}).dataType;
      if (dataType.java === DataType.VARCHAR.java) {
        return dataType.java;
      }
      return DataType.BIGINT.java;
    })();
    return this;
  }

  /**
   * 设置特定字段或枚举字段适配策略
   * @param adapters {Array<BaseAdapter>} 自定义枚举字段适配策略
   * @result {Table}
   */
  setAdapters(adapters = [new BaseAdapter()]) {
    /**
     * 自定义枚举字段适配策略
     * @return {Array<BaseAdapter>} 自定义枚举字段适配策略
     */
    this.adapters = adapters;
    return this;
  }

  /**
   * 设置输出包目录
   * @param module {string} 模块名
   * @result {Table}
   */
  setOutput(module) {
    /**
     * 输出路径
     * @type {string}
     */
    this.output = `${module}/src/test/java/${this.pkg.replace(/\./g, '/')}/code/${this.names.javaname}`;
    return this;
  }

  /**
   * 实体响应字段排序
   * @returns {string}
   */
  getEntityOrders() {
    return this.columns.map(({name}) => `"${name}"`).join(', ');
  }

  /**
   * 实体是否需要用户缓存
   * @returns {string}
   */
  getEntityITabUserCache() {
    return this.columns.some(({name}) => ['insertUserId', 'updateUserId'].includes(name)) ? 'ITabUserCache,' : '';
  }

// const ITimestamp (){return this.columns.some(({name}) => name === 'updateTime') ? 'ITimestamp, // 所有需要更新时间戳的实体类' : '';}

  /**
   * 写入实体
   * @param templateName {string} 模板目录
   * @return {Table}
   */
  async writeEntity(templateName) {
    const {Entity} = await import(`../${templateName}`);
    const filename = `entity/${this.names.TabName}.java`;
    const absolute = Paths.resolve(this.output, filename).mkdirsParent().absolute();
    console.log(absolute);
    const content = await Entity(this);
    fs.writeFileSync(absolute, content);
    return this;
  }

  /**
   * 写入 http 文件
   *
   * @param templateName {string} 模板目录
   * @return {Table}
   */
  async writeHttp(templateName) {
    const {Http} = await import(`../${templateName}`);
    const output = this.output.replace(/(\/src\/test\/)java\/.*$/, '$1');
    const filename = `http/${this.names.JavaName}Controller.http`;
    const absolute = Paths.resolve(output, filename).mkdirsParent().absolute();
    console.log(absolute);
    const content = await Http(this);
    fs.writeFileSync(absolute, content);
    return this;
  }

  /**
   * 写入 Controller
   *
   * @param templateName {string} 模板目录
   * @return {Table}
   */
  async writeController(templateName) {
    const {Controller} = await import(`../${templateName}`);
    const filename = `web/${this.names.JavaName}Controller.java`;
    const absolute = Paths.resolve(this.output, filename).mkdirsParent().absolute();
    console.log(absolute);
    const content = await Controller(this);
    fs.writeFileSync(absolute, content);
    return this;
  }

  /**
   * 写入 Service
   *
   * @param templateName {string} 模板目录
   * @return {Table}
   */
  async writeService(templateName) {
    const {Service} = await import(`../${templateName}`);
    const filename = `service/${this.names.JavaName}Service.java`;
    const absolute = Paths.resolve(this.output, filename).mkdirsParent().absolute();
    console.log(absolute);
    const content = await Service(this);
    fs.writeFileSync(absolute, content);
    return this;
  }

  /**
   * 写入 Repository
   *
   * @param templateName {string} 模板目录
   * @return {Table}
   */
  async writeRepository(templateName) {
    const {Repository} = await import(`../${templateName}`);
    const filename = `dao/jpa/${this.names.JavaName}Repository.java`;
    const absolute = Paths.resolve(this.output, filename).mkdirsParent().absolute();
    console.log(absolute);
    const content = await Repository(this);
    fs.writeFileSync(absolute, content);
    return this;
  }
}
