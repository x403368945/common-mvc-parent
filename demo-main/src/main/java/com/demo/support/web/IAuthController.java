package com.demo.support.web;

import com.demo.business.user.entity.TabUser;

/**
 * Controller 基础方法规范接口
 * 注释中有相应的代码实现模板，包括接参规范
 * 每个方法内部代码必须使用 try{}catch(){} 将所有异常变为可枚举的已知异常, 禁止 Controller 方法向外抛出异常
 *
 *
 * @author 谢长春 2017年7月14日 上午11:23:18
 */
public interface IAuthController<ID> extends com.mvc.web.IAuthController<TabUser, ID> {

}