/**
 *
 * @param table {Table}
 * @param auth {boolean} 接口是否需要鉴权
 * @param spare {boolean} 是否在生成代码时注释该方法，用于备用
 * @returns {string}
 */
const page = (table, {auth = false, spare = false}) => {
  const {
    comment,
    date,
    names: {TabName}
  } = table;
  const spareBegin = spare ? '/*' : '';
  const spareEnd = spare ? '*/' : '';
  const authUser = auth ? 'final TabUser user,' : '';
  return `${spareBegin}
    @GetMapping("/page/{number}/{size}")
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', '{}_page')") // <
    @ApiOperation(value = "6.分页查询${comment}", tags = {"${date}"})
    @ApiOperationSupport(
            order = 6,
            ignoreParameters = {"insertTime", "updateTime"}
    )
    @ResponseBody
    @Override
    public Result<${TabName}> page(${authUser}final int number, final int size, final ${TabName} condition) {
        return new Result<${TabName}>().execute(result -> result.setSuccess(service.findPage(
                Optional.ofNullable(condition).orElseGet(${TabName}::new),
                Pager.builder().number(number).size(size).build()
        )));
    }
${spareEnd}`
};
export const pageOpen = table => page(table, {auth: false, spare: false});
export const pageOpenSpare = table => page(table, {auth: false, spare: true});

export const pageAuth = table => page(table, {auth: true, spare: false});
export const pageAuthSpare = table => page(table, {auth: true, spare: true});
