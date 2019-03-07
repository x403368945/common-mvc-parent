package com.ccx.socket.ws.adapter;

import com.support.mvc.actions.ICommand;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 *
 * @author 谢长春 on 2017/12/11.
 */
public interface IAdapter {

    long getSessionId(final WebSocketSession session);

    void textMessage(final WebSocketSession session, final String jsonText) throws IOException;

    void binaryMessage(final WebSocketSession session, final BinaryMessage message) throws IOException;

    void cancel();

    default void process(final WebSocketSession session) {
    }

    void setServiceClass(final Class<? extends ICommand> clazz);

}
