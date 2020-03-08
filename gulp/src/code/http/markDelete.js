/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const markDelete = (table, {auth = false, spare = false}) => {
  const {
    comment,
    names: {java_name}
  } = table;
  const spareContent = spare ? '#' : '';
  return `### ${comment}: 按 id+uid 批量逻辑删除
${spareContent}PATCH http://{{host}}:{{port}}/1/${java_name}
${spareContent}Content-Type: application/json
${spareContent}${auth ? 'Authorization: Basic {{username}} {{password}}' : ''}
${spareContent}
${spareContent}[
${spareContent}  {
${spareContent}    "id": 0,
${spareContent}    "uid": ""
${spareContent}  }
${spareContent}]
`
};
export const markDeleteOpen = table => markDelete(table, {auth: false, spare: false});
export const markDeleteOpenSpare = table => markDelete(table, {auth: false, spare: true});

export const markDeleteAuth = table => markDelete(table, {auth: true, spare: false});
export const markDeleteAuthSpare = table => markDelete(table, {auth: true, spare: true});
