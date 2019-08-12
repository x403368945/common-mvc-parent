/**
 * 字符串原型扩展
 * * @author 谢长春 2019-7-28
 */
(() => {
  /**
   * 日期格式化
   * @param pattern {string} yyyy-MM-dd HH:mm:ss
   * @return {string}
   */
  Date.prototype.format = function (pattern) {
    const values = {
      'y+': this.getFullYear(), // 年份
      'M+': this.getMonth() + 1, // 月份
      'd+': this.getDate(), // 日
      'H+': this.getHours(), // 小时
      'm+': this.getMinutes(), // 分
      's+': this.getSeconds(), // 秒
      'q+': Math.floor((this.getMonth() + 3) / 3), // 季度
      'S+': this.getMilliseconds() // 毫秒
    };
    for (let key in values) {
      pattern = pattern.replace(new RegExp(key), (match) => {
        return values[key].toString().padStart(match.length, '0');
      });
    }
    return pattern;
  };

  /**
   * 日期格式化:yyyy-MM-dd
   * @returns {string}
   */
  Date.prototype.formatDate = function () {
    return this.format('yyyy-MM-dd');
  };

  /**
   * 日期格式化:yyyy-MM-dd HH:mm:ss
   * @returns {string}
   */
  Date.prototype.formatDatetime = function () {
    return this.format('yyyy-MM-dd HH:mm:ss');
  };

  /**
   * 日期格式化:yyyy-MM-dd HH:mm:ss.SSS
   * @returns {string}
   */
  Date.prototype.formatTimestamp = function () {
    return this.format('yyyy-MM-dd HH:mm:ss.SSS');
  };

  /**
   * 增加或减少{年}
   * @param value {int} 数字，正数表示增加，负数表示减少
   * @returns {Date}
   */
  Date.prototype.addYear = function (value = 0) {
    this.setFullYear(this.getFullYear() + value);
    return this;
  };

  /**
   * 增加或减少{月}
   * @param value {int} 数字，正数表示增加，负数表示减少
   * @returns {Date}
   */
  Date.prototype.addMonth = function (value = 0) {
    this.setMonth(this.getMonth() + value);
    return this;
  };

  /**
   * 增加或减少{天}
   * @param value {int} 数字，正数表示增加，负数表示减少
   * @returns {Date}
   */
  Date.prototype.addDay = function (value = 0) {
    this.setDate(this.getDate() + value);
    return this;
  };

  /**
   * 增加或减少{小时}
   * @param value {int} 数字，正数表示增加，负数表示减少
   * @returns {Date}
   */
  Date.prototype.addHour = function (value = 0) {
    this.setHours(this.getHours() + value);
    return this;
  };

  /**
   * 增加或减少{分钟}
   * @param value {int} 数字，正数表示增加，负数表示减少
   * @returns {Date}
   */
  Date.prototype.addMinutes = function (value = 0) {
    this.setMinutes(this.getMinutes() + value);
    return this;
  };

  /**
   * 增加或减少{秒}
   * @param value {int} 数字，正数表示增加，负数表示减少
   * @returns {Date}
   */
  Date.prototype.addSeconds = function (value = 0) {
    this.setSeconds(this.getSeconds() + value);
    return this;
  };

  /**
   * 增加或减少{毫秒}
   * @param value {int} 数字，正数表示增加，负数表示减少
   * @returns {Date}
   */
  Date.prototype.addMilliseconds = function (value = 0) {
    this.setMilliseconds(this.getMilliseconds() + value);
    return this;
  };
})();
