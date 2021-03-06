package com.ccx.socket.common.service;

import com.alibaba.fastjson.JSON;
import com.ccx.socket.utils.JSEngine;
import com.support.mvc.actions.ICallback;
import com.support.mvc.actions.ICommand;
import com.utils.exception.InfinityException;
import com.utils.exception.NaNException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;

/**
 * 计算服务
 *
 * @author 谢长春 2018/12/4
 */
@Slf4j
@Service
public class FormulaService implements ICommand {
    @Override
    public void command(final String body, final ICallback callback, final Long userId) {
        try {
            callback.start("开始计算");
            final String formula = JSON.parseObject(body).getString("formula");
            try {
                callback.info("{}={}", formula, JSEngine.getInstance().compute(formula));
            } catch (ScriptException e) {
                if (e instanceof NaNException) {
                    callback.error("计算失败，表达式存在非数字：{}", formula);
                } else if (e instanceof InfinityException) {
                    callback.error("计算失败，表达式除数不能为0：{}", formula);
                } else {
                    callback.error("计算失败，表达式不正确：{}", formula);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            callback.error("异常");
        } finally {
            callback.end("结束");
        }
    }
}
