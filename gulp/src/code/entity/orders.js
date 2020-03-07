/**
 * 实体响应字段排序
 * @param table {Table}
 * @returns {string}
 */
const orders = table => {
  const {columns} = table;
  return columns.map(({name}) => `"${name}"`).join(', ');
};
export default orders;
