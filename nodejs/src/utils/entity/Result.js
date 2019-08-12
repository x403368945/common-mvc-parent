import get from 'lodash/get';
import isUndefined from 'lodash/isUndefined';
import isNull from 'lodash/isNull';

/**
 * http 状态及说明
 * @author 谢长春 2019-7-28
 */
const httpStatus = Object.freeze({
  100: '（继续|CONTINUE）请求者应当继续提出请求。 服务器返回此代码表示已收到请求的第一部分，正在等待其余部分。',
  101: '（切换协议|WITCHING_PROTOCOLS）请求者已要求服务器切换协议，服务器已确认并准备切换。',
  // 200: '（成功|OK）服务器已成功处理了请求。通常，这表示服务器提供了请求的网页。',
  // 201:'（已创建|CREATED）请求成功并且服务器创建了新的资源。',
  // 202:'（已接受|ACCEPTED） 服务器已接受请求，但尚未处理。',
  // 203:'（非授权信息|NON_AUTHORITATIVE_INFORMATION）服务器已成功处理了请求，但返回的信息可能来自另一来源。',
  // 204:'（无内容|NO_CONTENT）服务器成功处理了请求，但没有返回任何内容。',
  // 205:'（重置内容|RESET_CONTENT）服务器成功处理了请求，但没有返回任何内容。',
  // 206:'（部分内容|PARTIAL_CONTENT）服务器成功处理了部分 GET 请求。',
  300: '（多种选择|MULTIPLE_CHOICES）针对请求，服务器可执行多种操作。 服务器可根据请求者 (user agent) 选择一项操作，或提供操作列表供请求者选择。',
  301: '（永久移动|MOVED_PERMANENTLY）请求的网页已永久移动到新位置。 服务器返回此响应（对 GET 或 HEAD 请求的响应）时，会自动将请求者转到新位置。',
  302: '（临时移动|MOVED_TEMPORARILY|FOUND）服务器目前从不同位置的网页响应请求，但请求者应继续使用原有位置来进行以后的请求。',
  303: '（查看其他位置|SEE_OTHER）请求者应当对不同的位置使用单独的 GET 请求来检索响应时，服务器返回此代码。',
  304: '（未修改|NOT_MODIFIED）自从上次请求后，请求的网页未修改过。 服务器返回此响应时，不会返回网页内容。',
  305: '（使用代理|USE_PROXY）请求者只能使用代理访问请求的网页。 如果服务器返回此响应，还表示请求者应使用代理。',
  307: '（临时重定向|TEMPORARY_REDIRECT）服务器目前从不同位置的网页响应请求，但请求者应继续使用原有位置来进行以后的请求。',

  400: '（错误请求|BAD_REQUEST）服务器不理解请求的语法。',
  401: '（未授权|UNAUTHORIZED）请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应。',
  402: '（|PAYMENT_REQUIRED）',
  403: '（禁止|FORBIDDEN）服务器拒绝请求。',
  404: '（未找到|NOT_FOUND）服务器找不到请求的网页。',
  405: '（方法禁用|METHOD_NOT_ALLOWED）禁用请求中指定的方法。',
  406: '（不接受|NOT_ACCEPTABLE）无法使用请求的内容特性响应请求的网页。',
  407: '（需要代理授权|PROXY_AUTHENTICATION_REQUIRED）此状态代码与 401（未授权）类似，但指定请求者应当授权使用代理。',
  408: '（请求超时|REQUEST_TIMEOUT）服务器等候请求时发生超时。',
  409: '（冲突|CONFLICT）服务器在完成请求时发生冲突。 服务器必须在响应中包含有关冲突的信息。',
  410: '（已删除|GONE）如果请求的资源已永久删除，服务器就会返回此响应。',
  411: '（需要有效长度|LENGTH_REQUIRED）服务器不接受不含有效内容长度标头字段的请求。',
  412: '（未满足前提条件|PRECONDITION_FAILED）服务器未满足请求者在请求中设置的其中一个前提条件。',
  413: '（请求实体过大|REQUEST_ENTITY_TOO_LARGE）服务器无法处理请求，因为请求实体过大，超出服务器的处理能力。',
  414: '（请求的 URI 过长|REQUEST_URI_TOO_LONG）请求的 URI（通常为网址）过长，服务器无法处理。',
  415: '（不支持的媒体类型|UNSUPPORTED_MEDIA_TYPE）请求的格式不受请求页面的支持。',
  416: '（请求范围不符合要求|REQUESTED_RANGE_NOT_SATISFIABLE）如果页面无法提供请求的范围，则服务器会返回此状态代码。',
  417: '（未满足期望值|EXPECTATION_FAILED）服务器未满足”期望”请求标头字段的要求。',
  428: '（要求先决条件|Precondition Required）',
  429: '（太多请求|Too Many Requests）',
  431: '（请求头字段太大|Request Header Fields Too Large）',

  500: '（服务器内部错误|INTERNAL_SERVER_ERROR）服务器遇到错误，无法完成请求。',
  501: '（尚未实施|NOT_IMPLEMENTED）服务器不具备完成请求的功能。 例如，服务器无法识别请求方法时可能会返回此代码。',
  502: '（错误网关|BAD_GATEWAY）服务器作为网关或代理，从上游服务器收到无效响应。',
  503: '（服务不可用|SERVICE_UNAVAILABLE）服务器目前无法使用（由于超载或停机维护）。 通常，这只是暂时状态。',
  504: '（网关超时|GATEWAY_TIMEOUT）服务器作为网关或代理，但是没有及时从上游服务器收到请求。',
  505: '（HTTP 版本不受支持|HTTP_VERSION_NOT_SUPPORTED）服务器不支持请求中所用的 HTTP 协议版本。',
  511: '（要求网络认证|Network Authentication Required）'
});

/**
 * HTTP 响应结果对象
 * @author 谢长春 2019-7-1
 */
export default class Result {
  /**
   * js 中， 类对象在经过方法传递后无法推断类型，造成类方法和变量提示不准确，这里 self 转换之后可以得到正确的提示
   * @param self {Result}
   * @return {Result}
   */
  static self(self) {
    return self;
  }

  /**
   * 静态构造函数，使用 http 响应结果构造响应对象
   * @param res {object} http 响应结果
   * @return {Result}
   */
  static ofResponse(res) {
    // eslint-disable-next-line no-unused-vars
    const {status, statusText, headers, config, request, data} = res || {};
    if (status >= 200 && status < 300) {
      return Result.of(Object.assign({res}, data));
    } else {
      return Result.of({
        message: '网络请求失败',
        exception: `响应状态码【${status}】:${httpStatus[status] || '不明确的 http 响应状态，请查看 Result.res 明细'}`,
        res
      })
    }
  }

  /**
   * 静态构造函数，使用 http 响应异常构造响应对象
   * @param res {object} http 响应结果
   * @return {Result}
   */
  static ofCatch(res) {
    return Result.of({
      message: '网络请求失败',
      exception: `响应状态码【${res.code}】:不明确的 http 响应状态，请查看 Result.res 明细`,
      res
    })
  }

  /**
   * 静态构造函数，带默认值，接收参数为对象，默认从对象中解构
   * @param v {number} 当前接口最新版本号
   * @param code {string} 后端响应状态
   * @param message {string} 后端响应消息，可直接弹窗给用户的消息
   * @param rowCount {number} 本次响应数据行数
   * @param pageCount {number} 分页：总页数
   * @param totalCount {number} 分页：总行数
   * @param rid {string} 本次请求代码
   * @param exception {string} 响应异常内容，用于开发调试，可能会携带堆栈信息，仅开发环境返回
   * @param data {Array} 后端响应数据
   * @param extras {object} 后端响应数据扩展
   * @param version {object} 版本信息，内含接口响应参数说明，，仅开发环境返回
   * @param res {object} 完整的 http 请求+响应结果
   * @return {Result}
   */
  static of({v = undefined, code = undefined, message = undefined, rowCount = undefined, pageCount = undefined, totalCount = undefined, rid = undefined, exception = undefined, data = undefined, extras = undefined, version = undefined, res = undefined} = {}) {
    return new Result(v, code, message, rowCount, pageCount, totalCount, rid, exception, data, extras, version, res);
  }

  /**
   * 默认构造函数
   * @param v {number} 当前接口最新版本号
   * @param code {string} 后端响应状态
   * @param message {string} 后端响应消息，可直接弹窗给用户的消息
   * @param rowCount {number} 本次响应数据行数
   * @param pageCount {number} 分页：总页数
   * @param totalCount {number} 分页：总行数
   * @param rid {string} 本次请求代码
   * @param exception {string} 响应异常内容，用于开发调试，可能会携带堆栈信息，仅开发环境返回
   * @param data {Array} 后端响应数据
   * @param extras {object} 后端响应数据扩展
   * @param version {object} 版本信息，内含接口响应参数说明，，仅开发环境返回
   * @param res {object} 完整的 http 请求+响应结果
   * @return {Result}
   */
  constructor(v, code = 'FAILURE', message = '失败', rowCount = 0, pageCount = 0, totalCount = 0, rid, exception, data, extras, version, res) {
    /**
     * 当前接口最新版本号
     * @type {number}
     */
    this.v = v;
    /**
     * 响应码
     * @type {string}
     */
    this.code = code;
    /**
     * 响应消息
     * @type {string}
     */
    this.message = message;
    /**
     * 本次响应数据行数
     * @type {number}
     */
    this.rowCount = rowCount;
    /**
     * 分页：总页数
     * @type {number}
     */
    this.pageCount = pageCount;
    /**
     * 分页：总行数
     * @type {number}
     */
    this.totalCount = totalCount;
    /**
     * 本次请求代码
     * @type {string}
     */
    this.rid = rid;
    /**
     * 响应异常内容，用于开发调试，可能会携带堆栈信息，仅开发环境返回
     * @type {string}
     */
    this.exception = exception;
    /**
     * 响应数据
     * @type {Array}
     */
    this.data = data;
    /**
     * 后端响应数据扩展
     * @type {Object}
     */
    this.extras = extras;
    /**
     * 版本信息，内含接口响应参数说明，，仅开发环境返回
     * @type {Object}
     */
    this.version = version;
    /**
     * 完整的 http 请求+响应结果
     * @type {Object}
     */
    this.res = res;
  }

  /**
   * 判断后端响应状态是否成功，【true：成功，false：失败】
   * @return {boolean}
   */
  ifSuccess() {
    return this.code === 'SUCCESS';
  }

  /**
   * 判断后端响应状态是否成功，且结果集是否有值
   * @return {boolean}
   */
  ifData() {
    return this.ifSuccess() && this.data && this.data.length > 0;
  }

  /**
   * 判断后端响应状态为成功时，执行 call(Result:当前 Result 对象) 方法，
   * 返回当前 {@link Result} 对象，便于链式调用和重复操作响应结果
   * @param call {Function<Result>}
   * @return {Result}
   */
  hasSuccess(call = (result) => {
  }) {
    if (this.ifSuccess()) call(this);
    return this;
  }

  /**
   * 判断后端响应状态为失败时，执行 call(Result:当前 Result 对象) 方法，
   * 返回当前 {@link Result} 对象，便于链式调用和重复操作响应结果
   * @param call {Function<Result>}
   * @return {Result}
   */
  hasFailure(call = (result) => {
  }) {
    if (!this.ifSuccess()) call(this);
    return this;
  }

  /**
   * 判断后端响应状态为成功时，执行 call(Result.data:当前 Result 对象中的 data 数据) 方法
   * 返回当前 {@link Result} 对象，便于链式调用和重复操作响应结果
   * @param call {Function<any>}
   * @return {Result}
   */
  hasData(call = (data) => {
  }) {
    call(this.ifSuccess() ? this.data || [] : []);
    return this;
  }

  /**
   * 判断后端响应状态为成功时，执行 call(Result.data:当前 Result 对象中的 data 数据) 方法
   * 返回当前 {@link Result} 对象，便于链式调用和重复操作响应结果
   * @param call {Function<any>}
   * @return {Result}
   */
  dataFirst(call = (obj) => {
  }) {
    if (this.ifSuccess() && this.data && this.data.length > 0) call(this.data[0]);
    return this;
  }

  /**
   * 获取 extras 指定 key 的值，当获取值为有效值时执行 call(value) 方法
   * 返回当前 {@link Result} 对象，便于链式调用和重复操作响应结果
   * @param key {string} key 可以是有层级的，例如: ‘parent.child.name’ 将会按层级获取对象中的属性 {parent:{child:{name:‘value’}}}，更详细的规则请参考 lodash 库中的 get 方法
   * @param nonNull {Function<any>} 参数非空时执行该方法
   * @param hasNull {Function<any>} 参数为空时执行该方法
   * @return {Result}
   */
  getExtras(key, nonNull = (data) => ({}), hasNull = () => ({})) {
    const value = get(key, this.extras);
    if (isUndefined(value) || isNull(value)) hasNull();
    else nonNull(value);
    return this;
  }

  /**
   * 获取返回的总页数和数据总行数填充到分页对象中
   * @param page {Page}
   * @return {Result}
   */
  mapPageCount(page) {
    page.pageCount = this.pageCount;
    page.totalCount = this.totalCount;
    return this;
  }

  /**
   * 断言响应代码，用于测试接口，直接抛出异常
   * @param code {string} 断言响应代码，可选参数，不选默认断言是否为 SUCCESS
   * @return {Result}
   */
  assertCode(code = 'SUCCESS') {
    if (code) {
      if (code !== this.code) throw new Error(`【${this.code}】响应代码异常，预期值【${code}】`)
    } else {
      this.hasFailure(result => {
        throw new Error(`【${this.code}】响应代码异常，预期值【SUCCESS】`)
      });
    }
    return this;
  }

  /**
   * 断言接口版本是否匹配，用于测试接口，直接抛出异常
   * @return {Result}
   */
  assertVersion() {
    this.getExtras('version', msg => {
      throw new Error(`接口版本不匹配，当前最新版本号【${this.v}】:${msg}`)
    });
    return this;
  }

  /**
   * 断言响应代码为成功且数据集合不能为空，用于测试接口，直接抛出异常
   * @return {Result}
   */
  assertData() {
    this.assertCode();
    if (!(this.data && this.data.length)) {
      throw new Error(`响应结果集合异常，预期结果集长度必须大于0`)
    }
    return this;
  }

  /**
   * 断言响应代码为成功且数据集合不能为空，且对第一条数据逻辑断言，用于测试接口，直接抛出异常
   * @param call {Function<any>} 该方法需要返回 Promise
   * @return {Result}
   */
  assertDataFirst(call = obj => {
  }) {
    this.assertCode();
    if (!(this.data && this.data.length)) {
      throw new Error(`响应结果集合异常，预期结果集长度必须大于0`)
    } else {
      const promise = call(this.data[0]);
      if (promise && promise instanceof Promise) {
        promise.catch(msg => {
          throw new Error(`响应结果第一条数据异常:${msg}`)
        })
      } else {
        throw new Error(`call 方法必须返回 Promise`)
      }
    }
    return this;
  }

  /**
   * 当前对象打印到控制台
   * @return {Result}
   */
  print() {
    console.debug(this.toString());
    return this;
  }

  /**
   * 将 data 以表格形式打印到控制台
   * @return {Result}
   */
  printTable() {
    console.table(this.data);
    return this;
  }

  toString() {
    return JSON.stringify({
      v: this.v,
      code: this.code,
      message: this.message,
      rowCount: this.rowCount,
      pageCount: this.pageCount,
      totalCount: this.totalCount,
      rid: this.rid,
      exception: this.exception,
      data: this.data,
      extras: this.extras,
      version: this.version
    })
  }
}
