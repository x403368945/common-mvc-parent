/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findByUid = (table, {auth = false, spare = false}) => {
  const {
    comment,
    names: {java_name}
  } = table;
  const spareContent = spare ? '#' : '';
  return `### ${comment}: 按 id+uid 查询单条记录
${spareContent}GET http://{{host}}:{{port}}/1/${java_name}/{{id}}/{{uid}}
${spareContent}Content-Type: application/json
${spareContent}${auth ? 'Authorization: Basic {{username}} {{password}}' : ''}
`
};
export const findByUidOpen = table => findByUid(table, {auth: false, spare: false});
export const findByUidOpenSpare = table => findByUid(table, {auth: false, spare: true});

export const findByUidAuth = table => findByUid(table, {auth: true, spare: false});
export const findByUidAuthSpare = table => findByUid(table, {auth: true, spare: true});
