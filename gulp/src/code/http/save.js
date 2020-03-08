/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const save = (table, {auth = false, spare = false}) => {
  const {
    comment,
    names: {java_name}
  } = table;
  const spareContent = spare ? '#' : '';
  return `### ${comment}: 保存
${spareContent}POST http://{{host}}:{{port}}/1/${java_name}
${spareContent}Content-Type: application/json
${spareContent}${auth ? 'Authorization: Basic {{username}} {{password}}' : ''}
${spareContent}
${spareContent}{
${spareContent}
${spareContent}}
`
};
export const saveOpen = table => save(table, {auth: false, spare: false});
export const saveOpenSpare = table => save(table, {auth: false, spare: true});

export const saveAuth = table => save(table, {auth: true, spare: false});
export const saveAuthSpare = table => save(table, {auth: true, spare: true});
