import {saveAuth} from '../controller/save';
import {updateAuth} from '../controller/update';
import {deleteByIdAuthSpare} from '../controller/deleteById';
import {deleteByUidAuthSpare} from '../controller/deleteByUid';
import {markDeleteByUidAuth} from '../controller/markDeleteByUid';
import {markDeleteByIdsAuthSpare} from '../controller/markDeleteByIds';
import {markDeleteAuth} from '../controller/markDelete';
import {findByIdAuthSpare} from '../controller/findById';
import {findByUidAuth} from '../controller/findByUid';
import {pageAuth} from '../controller/page';
import {searchAuthSpare} from '../controller/search';

/**
 *
 * @param table {Table}
 */
const Controller = (table) => {
  const {
    pkg,
    idType,
    comment,
    date,
    names: {javaname, TabName, JavaName, java_name}
  } = table;
  return `package ${pkg}.code.${javaname}.web;

import com.alibaba.fastjson.JSON;
import ${pkg}.code.${javaname}.entity.${TabName};
import ${pkg}.business.user.entity.TabUser;
import ${pkg}.code.${javaname}.service.${JavaName}Service;
import ${pkg}.business.user.web.IAuthController;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.support.mvc.entity.base.MarkDelete;
import com.support.mvc.entity.base.Pager;
import com.support.mvc.entity.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

/**
 * 请求操作响应：${comment}
 *
 * @author 谢长春 on ${date}
 */
@Api(tags = "${comment}")
//@ApiSort(1) // 控制接口排序 <
@RequestMapping("/1/${java_name}")
@Controller
@Slf4j
@RequiredArgsConstructor
public class ${JavaName}Controller implements IAuthController<${idType}, ${TabName}> {

    private final ${JavaName}Service service;

${saveAuth(table)}
${updateAuth(table)}
${deleteByIdAuthSpare(table)}
${deleteByUidAuthSpare(table)}
${markDeleteByUidAuth(table)}
${markDeleteByIdsAuthSpare(table)}
${markDeleteAuth(table)}
${findByIdAuthSpare(table)}
${findByUidAuth(table)}
${pageAuth(table)}
${searchAuthSpare(table)}
}
`;
};
export default Controller
