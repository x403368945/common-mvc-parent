/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const search = (table, {auth = false, spare = false}) => {
  const {
    comment,
    names: {java_name}
  } = table;
  const spareContent = spare ? '#' : '';
  return `### ${comment}: 列表查询，不分页
${spareContent}GET http://{{host}}:{{port}}/1/${java_name}
${spareContent}Content-Type: application/json
${spareContent}${auth ? 'Authorization: Basic {{username}} {{password}}' : ''}
`
};
export const searchOpen = table => search(table, {auth: false, spare: false});
export const searchOpenSpare = table => search(table, {auth: false, spare: true});

export const searchAuth = table => search(table, {auth: true, spare: false});
export const searchAuthSpare = table => search(table, {auth: true, spare: true});
