/**
 * Created by Conor Xie on 2018/4/1.
 */
import hasNull from './hasNull';

export default (value) => {
  if (hasNull(value)) return true;
  if (Array.isArray(this)) return value.length === 0;
  if (typeof value === 'string') return value.trim() === '';
  return false;
};
