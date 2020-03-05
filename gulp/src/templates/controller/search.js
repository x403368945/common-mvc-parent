/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const search = (table, {auth = false, spare = false}) => {
  const {
    comment,
    date,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? 'final TabUser user, ' : '';
  return `${spareBegin}
    // 非必要情况下不要开放列表查询方法，因为没有分页控制，容易内存溢出。大批量查询数据应该使用分页查询<
    @GetMapping
    // @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_search')")
    @ApiOperation(value = "7.分页查询${comment}", tags = {"${date}"})
    @ApiOperationSupport(
            order = 7,
            ignoreParameters = {"insertTime", "updateTime"}
    )
    @ResponseBody
    @Override
    public Result<${TabName}> search(${authUser}final ${TabName} condition) {
        return new Result<${TabName}>().execute(result -> result.setSuccess(service.findList(
                Optional.ofNullable(condition).orElseGet(${TabName}::new),
        )));
    }
${spareEnd}`
};
export const searchOpen = table => search(table, {auth: false, spare: false});
export const searchOpenSpare = table => search(table, {auth: false, spare: true});

export const searchAuth = table => search(table, {auth: true, spare: false});
export const searchAuthSpare = table => search(table, {auth: true, spare: true});
