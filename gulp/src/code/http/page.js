/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const page = (table, {auth = false, spare = false}) => {
  const {
    comment,
    names: {java_name}
  } = table;
  const spareContent = spare ? '#' : '';
  return `### ${comment}: 分页查询
${spareContent}GET http://{{host}}:{{port}}/1/${java_name}/page/1/10
${spareContent}Content-Type: application/json
${spareContent}${auth ? 'Authorization: Basic {{username}} {{password}}' : ''}
`
};
export const pageOpen = table => page(table, {auth: false, spare: false});
export const pageOpenSpare = table => page(table, {auth: false, spare: true});

export const pageAuth = table => page(table, {auth: true, spare: false});
export const pageAuthSpare = table => page(table, {auth: true, spare: true});
