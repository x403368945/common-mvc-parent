/**
 * Created by Jason Xie on 2018/4/1.
 */
import hasNull from './hasNull';

export default (obj = {}, key, defaultValue) => {
  const value = hasNull(obj[key]) ? defaultValue : obj[key];
  delete obj[key];
  return value;
};
