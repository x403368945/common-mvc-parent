/**
 * 实体是否需要用户缓存
 * @param table {Table}
 * @returns {string}
 */
const tabUserCache = table => {
  const {columns} = table;
  return columns.some(({name}) => ['insertUserId', 'updateUserId'].includes(name)) ? 'ITabUserCache,' : '';
};
export default tabUserCache;
