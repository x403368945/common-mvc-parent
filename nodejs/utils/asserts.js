/**
 * 断言工具类
 * Created by Jason Xie on 2018/4/1.
 */
import isNaN from 'lodash/isNaN';
import isFinite from 'lodash/isFinite';
import isEqual from 'lodash/isEqual';
import hasNull from './libs/hasNull';
import hasEmpty from './libs/hasEmpty';

export function nonNull(value, message) {
  return (hasNull(value)) ? Promise.reject(message) : Promise.resolve();
}

export function nonEmpty(value, message) {
  return hasEmpty(value) ? Promise.reject(message) : Promise.resolve();
}

export function hasNumber(value, message) {
  return (hasNull(value) || isNaN(value) || !isFinite(value)) ? Promise.reject(message) : Promise.resolve();
}

export function hasTrue(value, message) {
  return (value) ? Promise.resolve() : Promise.reject(message);
}

export function hasFalse(value, message) {
  return (value) ? Promise.resolve() : Promise.reject(message);
}

export function equals(a, b, message) {
  return isEqual(a, b) ? Promise.resolve() : Promise.reject(message);
}

export function range(v, [min, max], message) {
  return min <= v && v <= max ? Promise.resolve() : Promise.reject(message);
}

export function round(v, [min, max], message) {
  return min < v && v < max ? Promise.resolve() : Promise.reject(message);
}
console.log('fdsafa');

export function gtZore(value, message) {
  if (hasNull(value) || isNaN(value) || !isFinite(value)) return Promise.reject(message);
  return (value > 0) ? Promise.resolve() : Promise.reject(message);
  // return hasNumber(value, message)
  //     .then(() => {
  //         return (value > 0) ? Promise.resolve() : Promise.reject(message);
  //     });
}

export default {
  nonNull,
  nonEmpty,
  hasNumber,
  hasTrue,
  hasFalse,
  equals,
  range,
  round,
  gtZore
};
// { // nonNull
//     console.log(nonNull(null, 'is null'));
//     console.log(nonNull(undefined, 'is undefined'));
//     console.log(nonNull(NaN, 'is NaN'));
//     console.log(nonNull(Infinity, 'is Infinity'));
//     console.log(nonNull(0, 'is 0'));
//     console.log(nonNull(1, 'is 1'));
//     console.log(nonNull('', 'is Empty'));
// }
// { // nonEmpty
//     console.log(nonEmpty(null, 'is null'));
//     console.log(nonEmpty(undefined, 'is undefined'));
//     console.log(nonEmpty(NaN, 'is NaN'));
//     console.log(nonEmpty(Infinity, 'is Infinity'));
//     console.log(nonEmpty(0, 'is 0'));
//     console.log(nonEmpty(1, 'is 1'));
//     console.log(nonEmpty('', 'is Empty'));
//     console.log(nonEmpty({}, 'obj is Empty'));
//     console.log(nonEmpty([], 'Array is Empty'));
// }
// { // hasNumber
//     console.log(hasNumber(null, 'is null'));
//     console.log(hasNumber(undefined, 'is undefined'));
//     console.log(hasNumber(NaN, 'is NaN'));
//     console.log(hasNumber(Infinity, 'is Infinity'));
//     console.log(hasNumber(0, 'is 0'));
//     console.log(hasNumber(1, 'is 1'));
//     console.log(hasNumber('', 'is Empty'));
//     console.log(hasNumber({}, 'obj is Empty'));
//     console.log(hasNumber([], 'Array is Empty'));
// }
// { // hasTrue
//     console.log(hasTrue(null, 'is null'));
//     console.log(hasTrue(undefined, 'is undefined'));
//     console.log(hasTrue(NaN, 'is NaN'));
//     console.log(hasTrue(Infinity, 'is Infinity'));
//     console.log(hasTrue(0, 'is 0'));
//     console.log(hasTrue(1, 'is 1'));
//     console.log(hasTrue('', 'is Empty'));
//     console.log(hasTrue({}, 'obj is Empty'));
//     console.log(hasTrue([], 'Array is Empty'));
//     console.log(hasTrue(true, 'is true'));
//     console.log(hasTrue(false, 'is false'));
// }
// { // hasFalse
//     console.log(hasFalse(null, 'is null'));
//     console.log(hasFalse(undefined, 'is undefined'));
//     console.log(hasFalse(NaN, 'is NaN'));
//     console.log(hasFalse(Infinity, 'is Infinity'));
//     console.log(hasFalse(0, 'is 0'));
//     console.log(hasFalse(1, 'is 1'));
//     console.log(hasFalse('', 'is Empty'));
//     console.log(hasFalse({}, 'obj is Empty'));
//     console.log(hasFalse([], 'Array is Empty'));
//     console.log(hasFalse(true, 'is true'));
//     console.log(hasFalse(false, 'is false'));
// }
// { // gtZore
//     console.log(gtZore(null, 'is null'));
//     console.log(gtZore(undefined, 'is undefined'));
//     console.log(gtZore(NaN, 'is NaN'));
//     console.log(gtZore(Infinity, 'is Infinity'));
//     console.log(gtZore(0, 'is 0'));
//     console.log(gtZore(1, 'is 1'));
//     console.log(gtZore('', 'is Empty'));
//     console.log(gtZore({}, 'obj is Empty'));
//     console.log(gtZore([], 'Array is Empty'));
//     console.log(gtZore(true, 'is true'));
//     console.log(gtZore(false, 'is false'));
// }
