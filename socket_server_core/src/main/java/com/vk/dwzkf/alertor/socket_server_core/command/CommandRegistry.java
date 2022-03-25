package com.vk.dwzkf.alertor.socket_server_core.command;

import com.corundumstudio.socketio.SocketIOClient;
import com.vk.dwzkf.alertor.commons.entity.UserData;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import com.vk.dwzkf.alertor.socket_server_core.config.IOSocketServer;
import com.vk.dwzkf.alertor.socket_server_core.extractors.UserDataExtractor;
import com.vk.dwzkf.alertor.socket_server_core.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommandRegistry {
    private final IOSocketServer socketServer;
    private final UserDataExtractor<SocketIOClient> userDataExtractor;
    public static final Map<Class<? extends SocketCommand<?,?>>, String> eventMapping = new ConcurrentHashMap<>();
    public static final Map<Class<? extends SocketCommand<?,?>>, Class<?>> dataMapping = new ConcurrentHashMap<>();
    public static final Map<Class<? extends SocketCommand<?,?>>, String> callbackMapping = new ConcurrentHashMap<>();

    public static <T,R> void register(
            SocketApiConfig<T,R> config,
            Class<? extends SocketCommand<T,R>> socketCommandClass
    ) {
        eventMapping.put(socketCommandClass, config.getEventEndpoint());
        dataMapping.put(socketCommandClass, config.getRequestClass());
        if (config.getCallbackEndpoint() != null) {
            callbackMapping.put(socketCommandClass, config.getCallbackEndpoint());
        } else {
            log.warn("No responses for command {} cause no callback mapping configured.", socketCommandClass.getSimpleName());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void register(SocketCommand socketCommand) {
        if (eventMapping.get(socketCommand.getClass()) == null || dataMapping.get(socketCommand.getClass()) == null) {
            log.warn("Command {} ignored cause no mapping for data and event", socketCommand.getClass().getSimpleName());
            return;
        }
        String eventEndpoint = eventMapping.get(socketCommand.getClass());
        String callbackEndpoint = callbackMapping.get(socketCommand.getClass());
        socketServer.getServer()
                .addEventListener(
                        eventEndpoint,
                        dataMapping.get(socketCommand.getClass()),
                        ((client, data, ackSender) -> {
                            UserData userData = userDataExtractor.extract(client);
                            log.info("Received event from {}: {}", userData, data);
                            try {
                                Object executeResult = socketCommand.onEvent(userData, data);
                                if (callbackEndpoint != null) {
                                    if (executeResult != null) {
                                        client.sendEvent(callbackEndpoint, executeResult);
                                    }
                                } else {
                                    log.info("No callback sent to user for command {}, cause no config detected.", socketCommand.getClass().getSimpleName());
                                }
                            } catch (Exception e) {
                                ErrorResponse errorHandleResult = socketCommand.handleError(userData, eventEndpoint, data, e);
                                client.sendEvent("eventError", errorHandleResult);
                            }
                        })
                );
        log.info("Registered listener for EVENT_ENDPOINT:[{}], RESPONSE_ENDPOINT:[{}], DATA_TYPE:[{}]",
                eventEndpoint,
                callbackEndpoint == null ? "[NO_RESPONSE]" : callbackEndpoint,
                dataMapping.get(socketCommand.getClass()).getSimpleName()
        );
    }
}
