/**
 * Created by Jason Xie on 2018/4/1.
 */
/**
 * 打印 v 并返回v
 * @param v {Object}
 * @param peek {Function}
 * @returns {Object}
 */
export default (v, peek = (v) => {
  // console.log(v);
}) => {
  peek(v);
  return v;
};
