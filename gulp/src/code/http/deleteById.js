/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const deleteById = (table, {auth = false, spare = false}) => {
  const {
    comment,
    names: {java_name}
  } = table;
  const spareContent = spare ? '#' : '';
  return `### ${comment}: 按 id 物理删除
${spareContent}DELETE http://{{host}}:{{port}}/1/${java_name}/{{id}}
${spareContent}Content-Type: application/json
${spareContent}${auth ? 'Authorization: Basic {{username}} {{password}}' : ''}
`
};
export const deleteByIdOpen = table => deleteById(table, {auth: false, spare: false});
export const deleteByIdOpenSpare = table => deleteById(table, {auth: false, spare: true});

export const deleteByIdAuth = table => deleteById(table, {auth: true, spare: false});
export const deleteByIdAuthSpare = table => deleteById(table, {auth: true, spare: true});
