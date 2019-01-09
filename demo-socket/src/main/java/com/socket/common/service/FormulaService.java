package com.socket.common.service;

import com.mvc.actions.ICallback;
import com.mvc.actions.ICommand;
import com.mvc.entity.base.Param;
import com.utils.exception.InfinityException;
import com.utils.exception.NaNException;
import com.utils.util.JSEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;

/**
 * 计算服务
 *
 *
 * @author 谢长春 2018/12/4
 */
@Slf4j
@Service
public class FormulaService implements ICommand {
    @Override
    public void command(final Param param, final ICallback callback, final Long userId) {
        try {
            callback.start("开始计算");
            final String formula = param.parseObject().getString("formula");
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
