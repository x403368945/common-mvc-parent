/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const markDeleteById = (table, {auth = false, spare = false}) => {
  const {
    idType,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? ', final Long userId' : '';
  const authUserId = auth ? '\n                .set(q.updateUserId, userId)' : '';
  return `${spareBegin}
//     @CacheEvict(cacheNames = I${TabName}Cache.CACHE_ROW_BY_ID, key = "#id") // 若使用缓存需要解开代码 <
    @Override
    default long markDeleteById(final ${idType} id${authUser}) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Bool.YES)${authUserId}
                .where(q.id.eq(id).and(q.deleted.eq(Bool.NO)))
                .execute();
    }
${spareEnd}`
};
export const markDeleteByIdOpen = table => markDeleteById(table, {auth: false, spare: false});
export const markDeleteByIdOpenSpare = table => markDeleteById(table, {auth: false, spare: true});

export const markDeleteByIdAuth = table => markDeleteById(table, {auth: true, spare: false});
export const markDeleteByIdAuthSpare = table => markDeleteById(table, {auth: true, spare: true});
