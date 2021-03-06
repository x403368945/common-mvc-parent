/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const markDeleteById = (table, {auth = false, spare = false}) => {
  const {
    comment,
    names: {java_name}
  } = table;
  const spareContent = spare ? '#' : '';
  return `### ${comment}: 按 id 删除单条记录
${spareContent}PATCH http://{{host}}:{{port}}/1/${java_name}/{{id}}
${spareContent}Content-Type: application/json
${spareContent}${auth ? 'Authorization: Basic {{username}} {{password}}' : ''}
`
};
export const markDeleteByIdOpen = table => markDeleteById(table, {auth: false, spare: false});
export const markDeleteByIdOpenSpare = table => markDeleteById(table, {auth: false, spare: true});

export const markDeleteByIdAuth = table => markDeleteById(table, {auth: true, spare: false});
export const markDeleteByIdAuthSpare = table => markDeleteById(table, {auth: true, spare: true});
