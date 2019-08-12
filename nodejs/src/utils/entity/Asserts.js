/**
 * 参数断言工具类
 * Created by Conor Xie on 2018/4/1.
 */
import isEqual from 'lodash/isEqual';
import hasNull from '../libs/hasNull';
import hasEmpty from '../libs/hasEmpty';

export default class Asserts {
  /**
   * 静态构造函数
   * @return {Asserts}
   */
  static of() {
    return new Asserts();
  }

  /**
   * 注意 JS if 规则：
   * if([1, NaN, Infinity, true]) => true
   * if([0, null, undefined, false]) => false
   *
   * @param hasTrue {*} 为 true 时抛出异常消息
   * @param message {Function<string>} 异常消息
   * @return {Asserts}
   */
  hasTrue(hasTrue, message = () => '') {
    if (hasTrue) throw new Error(`参数校验失败:[js if 规则]:${message()}`);
    return this;
  }

  /**
   * 注意 JS if 规则：
   * if([1, NaN, Infinity, true]) => true
   * if([0, null, undefined, false]) => false
   *
   * @param hasFalse {*} 为 false 时抛出异常消息
   * @param message {Function<string>} 异常消息
   * @return {Asserts}
   */
  hasFalse(hasFalse, message = () => '') {
    if (!hasFalse) throw new Error(`参数校验失败:[js if 规则]:${message()}`);
    return this;
  }

  /**
   * 校验规则：null, undefined
   * @param value {*}
   * @param message {Function<string>} 异常消息
   * @return {Asserts}
   */
  hasNull(value, message = () => '') {
    if (hasNull(value)) throw new Error(`参数校验失败:[null,undefined]:${message()}`);
    return this;
  }

  /**
   * 校验规则：null, undefined,'',[],
   * @param value {*}
   * @param message {Function<string>} 异常消息
   * @return {Asserts}
   */
  hasEmpty(value, message = () => '') {
    if (hasEmpty(value)) throw new Error(`参数校验失败:[非空]:${message()}`);
    return this;
  }

  /**
   * 校验规则：!isEqual(left, right); 参考 locash 库：_.isEqual
   * @param left {*}
   * @param right {*}
   * @param message {Function<string>} 异常消息
   * @return {Asserts}
   */
  hasEquals(left, right, message = () => '') {
    if (!isEqual(left, right)) throw new Error(`参数校验失败:[相等]:${message()}`);
    return this;
  }

  /**
   * 校验规则：!(min <= value && value <= max)
   * @param value {number}
   * @param min {number}
   * @param max {number}
   * @param message {Function<string>} 异常消息
   * @return {Asserts}
   */
  hasRange(value, [min, max], message = () => '') {
    if (!(min <= value && value <= max)) throw new Error(`参数校验失败:表达式不成立[${min} <= ${value} && ${value} <= ${max}]:${message()}`);
    return this;
  }

  /**
   * 校验规则：!(min < value && value < max)
   * @param value {number}
   * @param min {number}
   * @param max {number}
   * @param message {Function<string>} 异常消息
   * @return {Asserts}
   */
  hasRound(value, [min, max], message = () => '') {
    if (!(min < value && value < max)) throw new Error(`参数校验失败:表达式不成立[${min} < ${value} && ${value} < ${max}]:${message()}`);
    return this;
  }

  /**
   * 校验规则： !(value > 0)
   * @param value {number}
   * @param message {Function<string>} 异常消息
   * @return {Asserts}
   */
  gtZero(value, message = () => '') {
    if (!(value > 0)) throw new Error(`参数校验失败:表达式不成立[${value} > 0]:${message()}`);
    return this;
  }

  /**
   * 校验规则：!(value >= 0)
   * @param value {number}
   * @param message {Function<string>} 异常消息
   * @return {Asserts}
   */
  geZero(value, message = () => '') {
    if (!(value >= 0)) throw new Error(`参数校验失败:表达式不成立[${value} >= 0]:${message()}`);
    return this;
  }
}
