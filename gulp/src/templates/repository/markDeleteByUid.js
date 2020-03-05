/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const markDeleteByUid = (table, {auth = false, spare = false}) => {
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
    default long markDeleteByUid(final ${idType} id, final String uid${authUser}) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Bool.YES)${authUserId}
                .where(q.id.eq(id).and(q.uid.eq(uid)).and(q.deleted.eq(Bool.NO)))
                .execute();
    }
${spareEnd}`
};
export const markDeleteByUidOpen = table => markDeleteByUid(table, {auth: false, spare: false});
export const markDeleteByUidOpenSpare = table => markDeleteByUid(table, {auth: false, spare: true});

export const markDeleteByUidAuth = table => markDeleteByUid(table, {auth: true, spare: false});
export const markDeleteByUidAuthSpare = table => markDeleteByUid(table, {auth: true, spare: true});
