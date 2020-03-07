/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findPage = (table, {auth = false, spare = false}) => {
  const {
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  return `${spareBegin}
    @Override // <
    default QueryResults<${TabName}> findPage(final ${TabName} condition, final Pager pager) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .selectFrom(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }
${spareEnd}`
};
export const findPageOpen = table => findPage(table, {auth: false, spare: false});
export const findPageOpenSpare = table => findPage(table, {auth: false, spare: true});

export const findPageAuth = table => findPage(table, {auth: true, spare: false});
export const findPageAuthSpare = table => findPage(table, {auth: true, spare: true});
