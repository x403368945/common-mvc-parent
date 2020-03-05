/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findList = (table, {auth = false, spare = false}) => {
  const {
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  return `${spareBegin}
    @Override // <
    default List<${TabName}> findList(final ${TabName} condition, final Expression<?>... exps) {
        return jpaQueryFactory.<JPAQueryFactory>get()
                .select(Projections.bean(${TabName}.class, exps))
                .from(q)
                .where(condition.where().toArray())
                .orderBy(condition.buildQdslSorts())
                .fetch();
    }
${spareEnd}`
};
export const findListOpen = table => findList(table, {auth: false, spare: false});
export const findListOpenSpare = table => findList(table, {auth: false, spare: true});

export const findListAuth = table => findList(table, {auth: true, spare: false});
export const findListAuthSpare = table => findList(table, {auth: true, spare: true});
