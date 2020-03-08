/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const update = (table, {auth = false, spare = false}) => {
  const {
    comment,
    names: {java_name}
  } = table;
  const spareContent = spare ? '#' : '';
  return `### ${comment}: 修改
${spareContent}PUT http://{{host}}:{{port}}/1/${java_name}/{{id}}
${spareContent}Content-Type: application/json
${spareContent}${auth ? 'Authorization: Basic {{username}} {{password}}' : ''}
${spareContent}
${spareContent}{
${spareContent}
${spareContent}}
`
};
export const updateOpen = table => update(table, {auth: false, spare: false});
export const updateOpenSpare = table => update(table, {auth: false, spare: true});

export const updateAuth = table => update(table, {auth: true, spare: false});
export const updateAuthSpare = table => update(table, {auth: true, spare: true});
