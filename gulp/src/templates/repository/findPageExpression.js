/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findPageExpression = (table, {auth = false, spare = false}) => {
  const {
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  return `${spareBegin}
    @Override // <
    default QueryResults<${TabName}> findPage(final ${TabName} condition, final Pager pager, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(${TabName}.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .offset(pager.offset())
                .limit(pager.limit())
                .orderBy(condition.buildQdslSorts())
                .fetchResults();
    }

${spareEnd}`
};
export const findPageExpressionOpen = table => findPageExpression(table, {auth: false, spare: false});
export const findPageExpressionOpenSpare = table => findPageExpression(table, {auth: false, spare: true});

export const findPageExpressionAuth = table => findPageExpression(table, {auth: true, spare: false});
export const findPageExpressionAuthSpare = table => findPageExpression(table, {auth: true, spare: true});
