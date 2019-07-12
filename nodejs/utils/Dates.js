const Pattern = Object.freeze({
  Year: 'yyyy',
  Month: 'yyyy-MM',
  Date: 'yyyy-MM-dd',
  DateTime: 'yyyy-MM-dd HH:mm:ss',
  Timestamp: 'yyyy-MM-dd HH:mm:ss.SSS'
});
export default class Dates {
  static pattern() {
    return Pattern;
  }

  /**
   * 静态构造
   * @param date {Date} 日期对象
   * @returns {Dates}
   */
  static of(date = new Date()) {
    return new Dates(date);
  }

  constructor(date = new Date()) {
    this._date = date;
    this._values = {
      'y+': this._date.getFullYear(), // 年份
      'M+': this._date.getMonth() + 1, // 月份
      'd+': this._date.getDate(), // 日
      'H+': this._date.getHours(), // 小时
      'm+': this._date.getMinutes(), // 分
      's+': this._date.getSeconds(), // 秒
      'q+': Math.floor((this._date.getMonth() + 3) / 3), // 季度
      'S+': this._date.getMilliseconds() // 毫秒
    };
  }

  format(pattern) {
    for (let key in this._values) {
      pattern = pattern.replace(new RegExp(key), (match) => {
        return this._values[key].toString().padStart(match.length, '0');
        // return ('000' + values[key].padStart(match.length, '0')).slice(-match.length);
      });
    }
    return pattern;
  }

  /**
   * 日期格式化:yyyy-MM-dd
   * @returns {String}
   */
  formatDate() {
    return this.format(Pattern.Date);
  }

  /**
   * 日期格式化:yyyy-MM-dd HH:mm:ss
   * @returns {String}
   */
  formatDatetime() {
    return this.format(Pattern.DateTime);
  }

  /**
   * 日期格式化:yyyy-MM-dd HH:mm:ss.SSS
   * @returns {String}
   */
  formatTimestamp() {
    return this.format(Pattern.Timestamp);
  }

  /**
   * 年
   * @param value {number} 增加或减少值；正值增加，负值减少
   * @returns {Dates}
   */
  addYear(value) {
    this._date.setFullYear(this._date.getFullYear() + value);
    return this;
  }

  /**
   * 月
   * @param value {number} 增加或减少值；正值增加，负值减少
   * @returns {Dates}
   */
  addMonth(value) {
    this._date.setMonth(this._date.getMonth() + value);
    return this;
  };

  /**
   * 日
   * @param value {number} 增加或减少值；正值增加，负值减少
   * @returns {Dates}
   */
  addDay(value) {
    this._date.setDate(this._date.getDate() + value);
    return this;
  };

  /**
   * 时
   * @param value {number} 增加或减少值；正值增加，负值减少
   * @returns {Dates}
   */
  addHour(value) {
    this._date.setHours(this._date.getHours() + value);
    return this;
  };

  /**
   * 分
   * @param value {number} 增加或减少值；正值增加，负值减少
   * @returns {Dates}
   */
  addMinutes(value) {
    this._date.setMinutes(this._date.getMinutes() + value);
    return this;
  };

  /**
   * 秒
   * @param value {number} 增加或减少值；正值增加，负值减少
   * @returns {Dates}
   */
  addSeconds(value) {
    this._date.setSeconds(this._date.getSeconds() + value);
    return this;
  };

  /**
   * 毫秒
   * @param value {number} 增加或减少值；正值增加，负值减少
   * @returns {Dates}
   */
  addMilliseconds(value) {
    this._date.setMilliseconds(this._date.getMilliseconds() + value);
    return this;
  };
}
