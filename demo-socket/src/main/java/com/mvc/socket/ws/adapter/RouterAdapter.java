package com.mvc.socket.ws.adapter;

import com.support.mvc.actions.ICallback;
import com.support.mvc.actions.ICommand;
import com.support.mvc.entity.base.Message;
import com.support.mvc.entity.base.Param;
import com.utils.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Objects;

import static com.mvc.socket.config.init.BeanInitializer.Beans.appContext;

/**
 *
 * @author 谢长春 2018/10/14
 */
@Slf4j
public class RouterAdapter implements IAdapter {
    protected Class<? extends ICommand> serviceClass;

    @Override
    public long getSessionId(WebSocketSession session) {
        return (long) Util.randomMax(10000);
    }

    @Override
    public void setServiceClass(final Class<? extends ICommand> serviceClass) {
        this.serviceClass = serviceClass;
    }

    @Override
    public void textMessage(final WebSocketSession session, final String jsonText) throws IOException {
        final ICommand command = appContext.getAppContext().getBean(serviceClass);
        command.command(Param.of(jsonText), new ICallback.AbstractCallback() {
            @Override
            public Message sendMessage(final Message message) {
                if (Objects.nonNull(logger)) logger.d(message.toString());
                else log.debug(message.toString());
                try {
                    session.sendMessage(new TextMessage(message.json()));
                } catch (IOException e) {
                    if (Objects.nonNull(logger)) logger.e(e.getMessage(), e);
                    else log.error(e.getMessage(), e);
                }
                return message;
            }
        }, getSessionId(session));
    }

    @Override
    public void binaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        log.debug("binaryMessage");
    }

    @Override
    public void cancel() {
        log.debug("cancel");
    }
}