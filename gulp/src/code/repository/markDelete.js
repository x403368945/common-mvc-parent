/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const markDelete = (table, {auth = false, spare = false}) => {
  const {
    idType,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? ', final Long userId' : '';
  const authUserId = auth ? '\n                .set(q.updateUserId, userId)' : '';
  return `${spareBegin}
    @Override // <
    default long markDelete(final List<MarkDelete> list${authUser}) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .update(q)
                .set(q.deleted, Bool.YES)${authUserId}
                .where(q.id.in(list.stream().map(MarkDelete::get${idType}Id).toArray(${idType}[]::new))
                        .and(q.deleted.eq(Bool.NO))
                        .and(q.uid.in(list.stream().map(MarkDelete::getUid).toArray(String[]::new)))
                )
                .execute();
    }
${spareEnd}`
};
export const markDeleteOpen = table => markDelete(table, {auth: false, spare: false});
export const markDeleteOpenSpare = table => markDelete(table, {auth: false, spare: true});

export const markDeleteAuth = table => markDelete(table, {auth: true, spare: false});
export const markDeleteAuthSpare = table => markDelete(table, {auth: true, spare: true});
