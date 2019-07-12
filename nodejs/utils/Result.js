/**
 * Http响应对象构建
 * Created by Jason Xie on 2017/5/24.
 */
import isArray from 'lodash/isArray';
import hasEmpty from './libs/hasEmpty';
import hasNull from './libs/hasNull';

const Codes = Object.freeze({
  success: {value: 'SUCCESS', message: '成功'},
  failure: {value: 'FAILURE', message: '失败'},
  timeout: {value: 'TIMEOUT', message: '网络请求超时'},
  httpStatus: {value: 'HTTP_STATUS', message: '网络请求失败'},
  paramsError: {value: 'PARAMS_ERROR', message: '参数错误'}
});
/**
 * 参考案例
 const result = new Result({});
 result.setCode('SUCCESS');
 result.setMessage('操作成功');
 console.log(result.toString());
 console.log(Result.of(JSON.stringify({code:'SUCCESS', message:'操作成功'})).toString());
 console.log(Result.success('测试成功').toString());
 console.log(Result.success([1,2,3]).toString());
 console.log(Result.failure('测试失败').toString());
 console.log(Result.error('ERROR', '异常', '参数异常').toString());
 console.log(Result.paramsError('字段【name】是必须的').toString());
 */
export default class Result {
  static of(json) {
    return new Result(JSON.parse(json));
  }

  /**
   * 静态构造函数
   * @param data {Object|Array} 数据集合
   * @returns {Result}
   */
  static success(data) {
    const array = hasEmpty(data) ? [] : isArray(data) ? data : [data];
    return new Result({
      code: Codes.success.value,
      message: Codes.success.message,
      data: array,
      rowCount: array.length
    });
  }

  /**
   * 静态构造函数
   * @param exception {String} 异常消息
   * @returns {Result}
   */
  static failure(exception) {
    return new Result({
      code: Codes.failure.value,
      message: Codes.failure.message,
      data: [],
      exception: exception
    });
  }

  /**
   * 静态构造函数
   * @param exception {String} 异常消息
   * @returns {Result}
   */
  static timeout(exception) {
    return new Result({
      code: Codes.timeout.value,
      message: Codes.timeout.message,
      data: [],
      exception: exception
    });
  }

  /**
   * 静态构造函数
   * @param status {int} 请求状态
   * @returns {Result}
   */
  static http(status) {
    return new Result({
      code: Codes.httpStatus.value,
      message: Codes.httpStatus.message,
      data: [],
      exception: 'http请求响应状态：{}'.format(status),
      extras: {status: status}
    });
  }

  /**
   * 静态构造函数
   * @param code {Codes} 异常代码
   * @param message {String} 弹窗消息
   * @param exception {String} 异常消息内容
   * @returns {Result}
   */
  static error(code, message, exception) {
    if (Codes.success.value === code) {
      code = Codes.failure.value;
      message = Codes.failure.message;
    }
    return new Result({
      code: code,
      message: message,
      data: [],
      exception: exception
    });
  }

  /**
   * 静态构造函数
   * @param exception {String} 异常消息
   * @returns {Result}
   */
  static paramsError(exception) {
    return new Result({
      code: Codes.paramsError.value,
      message: Codes.paramsError.message,
      data: [],
      exception: exception
    });
  }

  constructor({code = Codes.failure.value, message = Codes.failure.message, rowCount = 0, pageCount = 0, totalCount = 0, exception, data = [], extras}) {
    this.code = code;
    this.message = message;
    this.rowCount = rowCount || data.length;
    this.pageCount = pageCount;
    this.totalCount = totalCount;
    this.exception = exception;
    this.data = data;
    this.extras = extras;
  }

  /**
   * 获得响应编码
   * @returns {String}
   */
  getCode() {
    return this.code;
  }

  /**
   * 设置状态编码
   * @param value {String}
   * @returns {Result}
   */
  setCode(value) {
    this.code = value;
    return this;
  }

  /**
   * 获得响应消息
   * @returns {String}
   */
  getMessage() {
    return this.message;
  }

  /**
   * 设置弹出消息
   * @param value {String}
   * @returns {Result}
   */
  setMessage(value) {
    this.message = value;
    return this;
  }

  /**
   * 获得本次返回数据行数
   * @returns {int}
   */
  getRowCount() {
    return this.rowCount;
  }

  /**
   * 设置本次返回行数
   * @param value {int}
   * @returns {Result}
   */
  setRowCount(value) {
    this.rowCount = value;
    return this;
  }

  /**
   * 获取总页数
   * @returns {int}
   */
  getPageCount() {
    return this.pageCount;
  }

  /**
   * 设置总页数
   * @param value {int}
   * @returns {Result}
   */
  setPageCount(value) {
    this.pageCount = value;
    return this;
  }

  /**
   * 获得总行数
   * @returns {Number}
   */
  getTotalCount() {
    return this.totalCount;
  }

  /**
   * 设置总行数
   * @param value {int}
   * @returns {Result}
   */
  setTotalCount(value) {
    this.totalCount = value;
    return this;
  }

  /**
   * 获得异常消息内容
   * @returns {*}
   */
  getException() {
    return this.exception;
  }

  /**
   * 设置异常消息
   * @param value {String}
   * @returns {Result}
   */
  setException(value) {
    this.exception = value;
    return this;
  }

  /**
   * 获取响应结果
   * @returns {Array<*>}
   */
  getData() {
    return this.data;
  }

  /**
   * 设置数据集合
   * @param value {Array<Object>}
   * @returns {Result}
   */
  setData(value) {
    this.data = value;
    return this;
  }

  /**
   * 获得附加参数
   * @returns {Object}
   */
  getExtras() {
    return this.extras;
  }

  /**
   * 设置扩展属性
   * @param value {Object}
   * @returns {Result}
   */
  setExtras(value) {
    this.extras = value;
    return this;
  }

  /**
   * true:成功， false：失败
   * @returns {Boolean}
   */
  isSuccess() {
    return Codes.success.value === this.code.toUpperCase();
  }

  /**
   * 设置成功后的数据集合
   * @param data {Object}
   * @returns {Result}
   */
  setSuccess(data) {
    this.code = Codes.success.value;
    this.message = Codes.success.message;
    {
      if (hasEmpty(data)) this.data = [];
      else this.data = isArray(data) ? data : [data];
    }
    this.rowCount = this.data.length;
    return this;
  }

  /**
   * 设置扩展属性
   * @param key {String}
   * @param value {Array|Object}
   * @returns {Result}
   */
  addExtras(key, value) {
    if (hasNull(this.extras)) this.extras = {};
    this.extras[key] = value;
    return this;
  }

  /**
   * 当code = FAILURE 时，弹出 msg 指定的内容；否则直接弹出响应对象中的message
   * @param msg {String}
   * @returns {String}
   */
  getFailureMessage(msg) {
    return (msg && this.code === Codes.failure.value) ? msg : this.message;
  }

  /**
   * 获得数据集合中的第一项
   * @returns {Promise<*>}
   */
  dataFirst() {
    return (this.isSuccess() && this.data.length > 0)
      ? Promise.resolve(this.data[0])
      : Promise.reject('无数据：'.concat(this.toString()));
  }

  /**
   * 转换为对象
   * @returns {{code: String, message: String, rowCount: int, pageCount: int, totalCount: int, exception: String, data: Object|Array, extras: {}}}
   */
  toObject() {
    return {
      code: this.code,
      message: this.message,
      rowCount: this.rowCount,
      pageCount: this.pageCount,
      totalCount: this.totalCount,
      exception: this.exception,
      data: this.data,
      extras: this.extras
    };
  }

  toString(foramt) {
    return JSON.stringify(this.toObject(), null, foramt ? 2 : undefined);
  }
}
