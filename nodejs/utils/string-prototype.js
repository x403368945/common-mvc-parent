/**
 * 字符串原型扩展
 */
(() => {
  /**
   * 字符串格式化,使用{}占位，{}内可以为空，替换顺序为参数顺序
   * console.log('test/{d}/{ab}/{}/{123}'.format(1, 2, 3, 0));
   * @param args {Array}
   * @return {String}
   */
  String.prototype.format = function (...args) {
    return this.replace(/({(\w+)?})/gi, (match, $1) => args.length > 0 ? args.shift() : $1);
  };
  /**
   * 字符串格式化,使用{}占位，{}内不能为空，在字符串中检索{}内的属性，从object中获取对应的值
   * console.log('test/{d}/{ab}/{c}/{123}'.formatObject({d: 1, ab: 2, c: 3, 123: 0}));
   * @param object {Object}
   * @return {String}
   */
  String.prototype.formatObject = function (object = {}) {
    return this.replace(/({(\w+)?})/gi, (match, $1, $2) => object[$2] === undefined || object[$2] === null ? $1 : object[$2]);
  };
})();
