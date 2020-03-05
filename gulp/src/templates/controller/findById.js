/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const findById = (table, {auth = false, spare = false}) => {
  const {
    comment,
    idType,
    date,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? 'final TabUser user,' : '';
  return `${spareBegin}
    // 优先使用 findByUid 方法，可以阻止平行越权。 只有在实体没有 uid 的情况才能将该方法开放给前端<
    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_find')")
    @ApiOperation(value = "5.按 id 查询${comment}", tags = {"${date}"})
    @ApiOperationSupport(order = 5) // order id 相同的接口只能开放一个
    @ResponseBody
    @Override
    public Result<${TabName}> findById(${authUser}final ${idType} id) {
        return new Result<${TabName}>().execute(result -> result.setSuccess(service.findById(id).orElse(null)));
    }
${spareEnd}`
};
export const findByIdOpen = table => findById(table, {auth: false, spare: false});
export const findByIdOpenSpare = table => findById(table, {auth: false, spare: true});

export const findByIdAuth = table => findById(table, {auth: true, spare: false});
export const findByIdAuthSpare = table => findById(table, {auth: true, spare: true});
