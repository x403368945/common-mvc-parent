/**
 * 封装 http 请求
 * Created by Administrator on 2018/4/1.
 */
import axios from 'axios';
import {write} from './logs';
import Result from './Result';

export default class Http {
  constructor(url, config = {}) {
    this._url = url;
    this._config = config;
    this._log = {};
    this._baseUrl = axios.defaults.baseURL;
  }

  /**
   * post 请求
   * @param json {Object|Array}
   * @returns {Promise<Http>}
   */
  async post(json) {
    if (!process.env.PRODUCTION) {
      this._log = {
        url: undefined,
        BaseURL: this._baseUrl,
        URL: `${this._baseUrl}`.concat(this._url.formatObject(json)),
        Method: 'POST',
        Parameters: {json},
        Result: undefined
      };
    }
    const res = await axios.post(
      this._url.formatObject(json),
      {json},
      this._config
    );
    this._result = res.data;
    return this;
  }

  /**
   * put 请求
   * @param json {Object|Array}
   * @returns {Promise<Http>}
   */
  async put(json) {
    if (!process.env.PRODUCTION) {
      this._log = {
        BaseURL: this._baseUrl,
        URL: `${this._baseUrl}`.concat(this._url.formatObject(json)),
        Method: 'PUT',
        Parameters: {json},
        Result: undefined
      };
    }
    const res = await axios.put(
      this._url.formatObject(json),
      {json},
      this._config
    );
    this._result = res.data;
    return this;
  }

  /**
   * patch 请求
   * @param json {Object|Array}
   * @returns {Promise<Http>}
   */
  async patch(json) {
    if (!process.env.PRODUCTION) {
      this._log = {
        BaseURL: this._baseUrl,
        URL: `${this._baseUrl}`.concat(this._url.formatObject(json)),
        Method: 'PATCH',
        Parameters: {json},
        Result: undefined
      };
    }
    const res = await axios.patch(
      this._url.formatObject(json),
      {json},
      this._config
    );
    this._result = res.data;
    return this;
  }

  /**
   * delete 请求
   * @param json {Object|Array}
   * @returns {Promise<Http>}
   */
  async delete(json) {
    if (!process.env.PRODUCTION) {
      this._log = {
        BaseURL: this._baseUrl,
        URL: `${this._baseUrl}`.concat(this._url.formatObject(json)),
        Method: 'DELETE',
        Parameters: {json},
        Result: undefined
      };
    }
    const res = await axios.delete(
      this._url.formatObject(json),
      Object.assign({data: {json}}, this._config)
    );
    this._result = res.data;
    return this;
  }

  /**
   * get 请求
   * @param json {Object|Array}
   * @returns {Promise<Http>}
   */
  async get(json = {}) {
    if (!process.env.PRODUCTION) {
      this._log = {
        BaseURL: this._baseUrl,
        URL: `${this._baseUrl}`.concat(this._url.formatObject(json)).concat('?json='.concat(encodeURI(JSON.stringify(json)))),
        Method: 'GET',
        Parameters: {json},
        Result: undefined
      };
    }
    const res = await axios.get(
      this._url.formatObject(json),
      Object.assign({params: {json: encodeURI(JSON.stringify(json))}}, this._config)
    );
    this._result = res.data;
    return this;
  }

  // updateById(id){}
  /**
   * 物理删除：单条
   * @param id {String}
   * @returns {Promise<Http>}
   */
  async deleteById(id) {
    if (!process.env.PRODUCTION) {
      this._log = {
        BaseURL: this._baseUrl,
        URL: `${this._baseUrl}`.concat(this._url.format(id)),
        Method: 'DELETE',
        Parameters: {},
        Result: undefined
      };
    }
    const res = await axios.delete(
      this._url.format(id),
      this._config
    );
    this._result = res.data;
    return this;
  }

  /**
   * 逻辑删除：单条
   * @param id {String}
   * @returns {Promise<Http>}
   */
  async markDeleteById(id) {
    if (!process.env.PRODUCTION) {
      this._log = {
        BaseURL: this._baseUrl,
        URL: `${this._baseUrl}`.concat(this._url.format(id)),
        Method: 'PATCH',
        Parameters: {},
        Result: undefined
      };
    }
    const res = await axios.patch(
      this._url.format(id),
      {},
      this._config
    );
    this._result = res.data;
    return this;
  }

  /**
   * 逻辑删除：批量
   * @param ids {Array}
   * @returns {Promise<Http>}
   */
  async markDeleteByIds(ids) {
    if (!process.env.PRODUCTION) {
      this._log = {
        BaseURL: this._baseUrl,
        URL: `${this._baseUrl}`.concat(this._url),
        Method: 'PATCH',
        Parameters: {},
        Result: undefined
      };
    }
    const res = await axios.patch(
      this._url,
      {json: ids},
      this._config
    );
    this._result = res.data;
    return this;
  }

  /**
   * 按ID查询数据
   * @param id {String}
   * @returns {Promise<Http>}
   */
  async getById(id) {
    if (!process.env.PRODUCTION) {
      this._log = {
        BaseURL: this._baseUrl,
        URL: `${this._baseUrl}`.concat(this._url.format(id)),
        Method: 'GET',
        Parameters: {},
        Result: undefined
      };
    }
    const res = await axios.get(
      this._url.format(id),
      this._config
    );
    this._result = res.data;
    return this;
  }

  // search(pageIndex, pageSize, json = {}) {
  //     return this.get(Object.assign({pageIndex:pageIndex, pageSize:pageSize}, json))
  // }

  /**
   * 日志打印
   * @param filename {String} 代码所在文件名
   * @param name {String} 代码函数名
   * @param url {String} 请求url
   * @returns {Http}
   */
  log(filename, name, url) {
    if (!process.env.PRODUCTION) {
      this._log.url = url;
      this._log.Result = this._result;
      write(true, filename, name, this._log);
    }
    return this;
  }

  /**
   * @returns {Result}
   */
  getResult() {
    return new Result(this._result);
  }

  toString() {
    return JSON.stringify(this);
  }
}
// export default Http
