package com.mvc.actions;

import com.mvc.entity.base.Message;

/**
 * 动作行为组件接口：模型注册或更新 <br>
 * 可以通过 applicationContext.getBeansOfType(IRegist.class) ； 获取有所实现 IRegist 接口的类 <br>
 *  applicationContext.getBeansOfType(IRegist.class)
 *                 .values()
 *                 .map(IRegist::regist);
 *
 * @author 谢长春 on 2017/11/14.
 */
public interface IRegist {
    /**
     * 注册模型，返回注册状态消息
     *
     * @return {@link Message}
     */
    Message regist();
}