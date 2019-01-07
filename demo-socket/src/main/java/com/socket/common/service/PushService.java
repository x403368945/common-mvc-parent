package com.socket.common.service;

import com.mvc.actions.ICallback;
import com.mvc.actions.ICommand;
import com.mvc.entity.base.Message;
import com.mvc.entity.base.Param;
import com.utils.util.Dates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.mvc.actions.ICallback.Event.INFO;
import static com.socket.config.init.BeanInitializer.Beans.singleThread;

/**
 * 推送服务
 *
 * @author 谢长春 2018/12/4
 */
@Slf4j
@Service
public class PushService implements ICommand {
    @Override
    public void command(final Param param, final ICallback callback, final Long userId) {
        try {
            callback.start("开始推送");
            callback.sendMessage(INFO, "参数", param.parseObject());
            for (int i = 1; i <= 10; i++) {
                final int id = i;
                final Future<Message> submit = singleThread.<ExecutorService>get().submit(() -> {
                    Thread.sleep(1000);
                    return Message.builder()
                            .event(INFO)
                            .message("消息推送")
                            .build()
                            .addExtras("id", id)
                            .addExtras("timestamp", Dates.now().formatDateTime());
                });
                callback.sendMessage(submit.get());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            callback.error("推送异常");
        } finally {
            callback.end("推送结束");
        }
    }
}