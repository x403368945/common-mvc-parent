/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findCacheById = (table, {auth = false, spare = false}) => {
  const {
    idType,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  return `${spareBegin}
//     @Cacheable(cacheNames = I${TabName}Cache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码
     default ${TabName} findCacheById(final ${idType} id){
         return findById(id).orElse(null);
     }
${spareEnd}`
};
export const findCacheByIdOpen = table => findCacheById(table, {auth: false, spare: false});
export const findCacheByIdOpenSpare = table => findCacheById(table, {auth: false, spare: true});

export const findCacheByIdAuth = table => findCacheById(table, {auth: true, spare: false});
export const findCacheByIdAuthSpare = table => findCacheById(table, {auth: true, spare: true});
