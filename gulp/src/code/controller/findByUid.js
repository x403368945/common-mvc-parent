/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findByUid = (table, {auth = false, spare = false}) => {
  const {
    comment,
    idType,
    date,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? 'final TabUser user, ' : '';
  return `${spareBegin}
    @GetMapping("/{id}/{uid}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_find')")
    @ApiOperation(value = "5.按 id 和 uid 查询${comment}", tags = {"${date}"})
    @ApiOperationSupport(order = 5) // order id 相同的接口只能开放一个<
    @ResponseBody
    @Override
    public Result<${TabName}> findByUid(${authUser}final ${idType} id, final String uid) {
        return new Result<${TabName}>().execute(result -> result.setSuccess(service.findByUid(id, uid).orElse(null)));
    }
${spareEnd}`
};
export const findByUidOpen = table => findByUid(table, {auth: false, spare: false});
export const findByUidOpenSpare = table => findByUid(table, {auth: false, spare: true});

export const findByUidAuth = table => findByUid(table, {auth: true, spare: false});
export const findByUidAuthSpare = table => findByUid(table, {auth: true, spare: true});
