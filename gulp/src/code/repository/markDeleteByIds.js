/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const markDeleteByIds = (table, {auth = false, spare = false}) => {
  const {
    idType
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? ', final Long userId' : '';
  const authUserId = auth ? '\n                .set(q.updateUserId, userId)' : '';
  return `${spareBegin}
    @Override // <
    default long markDeleteByIds(final Set<${idType}> ids${authUser}) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Bool.YES)${authUserId}
                .where(q.id.in(ids).and(q.deleted.eq(Bool.NO)))
                .execute();
    }
${spareEnd}`
};
export const markDeleteByIdsOpen = table => markDeleteByIds(table, {auth: false, spare: false});
export const markDeleteByIdsOpenSpare = table => markDeleteByIds(table, {auth: false, spare: true});

export const markDeleteByIdsAuth = table => markDeleteByIds(table, {auth: true, spare: false});
export const markDeleteByIdsAuthSpare = table => markDeleteByIds(table, {auth: true, spare: true});
