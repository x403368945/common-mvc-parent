package com.mvc.socket.config;

import com.mvc.socket.ws.handler.RouterHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * @author 谢长春
 */
@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.debug("registerWebSocketHandlers");
        registry
                .addHandler(routerHander(), "/socket/router")
                .setAllowedOrigins("*")
//        	.setAllowedOrigins("http://mydomain.com")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
        ;
    }

    @Bean
    public WebSocketHandler routerHander() {
        return new RouterHandler();
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8 * 100 * 1024);
        container.setMaxBinaryMessageBufferSize(8 * 100 * 1024);
        return container;
    }

}