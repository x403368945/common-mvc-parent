/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @returns {string}
 */
const save = (table, {auth = false}) => {
  const {
    comment
  } = table;
  return `### 保存${comment}
POST http://{{host}}:{{port}}/demo-list/1
Content-Type: application/json
${auth ? 'Authorization: Basic {{username}} {{password}}' : ''}

{

}
`
};
export const saveOpen = table => save(table, {auth: false});
export const saveOpenSpare = table => save(table, {auth: false});

export const saveAuth = table => save(table, {auth: true});
export const saveAuthSpare = table => save(table, {auth: true});
