package com.vk.dwzkf.alertor.alertor_client_core.config;

import com.vk.dwzkf.alertor.alertor_client_core.handlers.EventHandler;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class EventHandlerRegistry {
    public static final Map<String, Class<? extends EventHandler<?>>> handlerRegistry = new ConcurrentHashMap<>();

    public static <T,R> void configureHandler(SocketApiConfig<T,R> config, Class<? extends EventHandler<R>> handlerClass) {
        handlerRegistry.put(config.getCallbackEndpoint(), handlerClass);
    }

    private Map<Class<?>, EventHandler<?>> eventHandlers = new ConcurrentHashMap<>();

    @Autowired(required = false)
    public void configureHandlers(List<EventHandler<?>> eventHandlers) {
        eventHandlers.forEach(handler -> EventHandlerRegistry.this.eventHandlers.put(handler.getClass(), handler));
    }

    public void configure(Socket socketClient) {
        handlerRegistry.forEach((key, handlerClass) -> {
            EventHandler<?> eventHandler = eventHandlers.get(handlerClass);
            if (eventHandler != null) {
                socketClient.on(key, eventHandler);
                log.info("Configure handler[{}] for event[{}]", eventHandler.getClass().getSimpleName(), key);
            } else {
                log.warn("No handler for event[{}] found. skipped", key);
            }
        });
    }
}
