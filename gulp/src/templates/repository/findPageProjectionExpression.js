/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findPageProjectionExpression = (table, {auth = false, spare = false}) => {
  const {
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  return `${spareBegin}
    @Override // <
    default <T extends ${TabName}> QueryResults<T> findPageProjection(final ${TabName} condition, final Pager pager, final Class<T> clazz, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(clazz, exps))
                .from(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }
${spareEnd}`
};
export const findPageProjectionExpressionOpen = table => findPageProjectionExpression(table, {auth: false, spare: false});
export const findPageProjectionExpressionOpenSpare = table => findPageProjectionExpression(table, {auth: false, spare: true});

export const findPageProjectionExpressionAuth = table => findPageProjectionExpression(table, {auth: true, spare: false});
export const findPageProjectionExpressionAuthSpare = table => findPageProjectionExpression(table, {auth: true, spare: true});
