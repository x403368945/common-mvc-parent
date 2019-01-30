package com.mvc.socket.ws.handler;

import com.alibaba.fastjson.JSON;
import com.mvc.socket.common.service.FormulaService;
import com.mvc.socket.common.service.PushService;
import com.mvc.socket.ws.adapter.IAdapter;
import com.mvc.socket.ws.adapter.RouterAdapter;
import com.support.mvc.actions.ICommand;
import com.support.mvc.entity.base.Message;
import com.utils.util.Util;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import static com.support.mvc.actions.ICallback.Event.END;
import static com.support.mvc.actions.ICallback.Event.ERROR;

/**
 * socket 分发路由
 *
 * @author 谢长春
 */
@Slf4j
public class RouterHandler extends AbstractWebSocketHandler {

    private enum Modules {
        FORMULA("计算", RouterAdapter.class, FormulaService.class),
        PUSH("推送", RouterAdapter.class, PushService.class),
        ;
        final String comment;
        final Class<? extends IAdapter> adapter;
        final Class<? extends ICommand> service;

        Modules(final String comment, Class<? extends IAdapter> adapter, Class<? extends ICommand> service) {
            this.comment = comment;
            this.adapter = adapter;
            this.service = service;
        }

        @SneakyThrows
        IAdapter getAdapter() {
            return adapter.newInstance();
        }

        Class<? extends ICommand> getServiceClass() {
            return service;
        }
    }

    @Data
    static class Payload {
        /**
         * 模型枚举
         */
        String module;
        /**
         * 参数集合：json
         */
        String json;

        public Modules parseModule() {
            return Modules.valueOf(module);
        }
    }

    private IAdapter adapter = null;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @SneakyThrows
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            final Payload payload = JSON.parseObject(message.getPayload(), Payload.class);
            if (Util.isEmpty(payload.getModule())) {
                session.sendMessage(new TextMessage(Message.builder().event(ERROR).message("module 参数是必须的").build().json()));
                throw new NullPointerException("module 参数是必须的");
            }
            if (Util.isEmpty(payload.getJson())) {
                session.sendMessage(new TextMessage(Message.builder().event(ERROR).message("json 参数是必须的").build().json()));
                throw new NullPointerException("json 参数是必须的");
            }
            try {
                final Modules module = payload.parseModule();
                adapter = module.getAdapter();
                adapter.setServiceClass(module.getServiceClass());
                adapter.textMessage(session, payload.getJson());
            } catch (Exception e) {
                session.sendMessage(new TextMessage(Message.builder().event(ERROR).message("未知的 module").build().json()));
                throw e;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            session.sendMessage(new TextMessage(Message.builder().event(END).build().json()));
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        super.handleBinaryMessage(session, message);
        adapter.binaryMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        log.debug("socket close");
        adapter.cancel();
        adapter = null;
        System.gc();
    }
}