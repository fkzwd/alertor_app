package com.vk.dwzkf.alertor.socket_server_core.config;

import com.corundumstudio.socketio.SocketIOClient;
import com.vk.dwzkf.alertor.commons.entity.UserData;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventSender {
    private final IOSocketServer ioSocketServer;

    public <T, R> void sendEvent(UserData userData, SocketApiConfig<T, R> config, R data) {
        try {
            ioSocketServer.getServer()
                    .getClient(UUID.fromString(userData.getUuid()))
                    .sendEvent(config.getCallbackEndpoint(), data);
        } catch (Exception exception) {
            log.error("Error while send event to endpoint[{}] for[{}]. Data:[{}]",
                    config.getCallbackEndpoint(),
                    userData.getName(),
                    data
            );
        }
    }

    public <T,R> void broadcast(UserData excludeUser, SocketApiConfig<T,R> config, R data) {
        if (excludeUser == null) {
            ioSocketServer.getServer().getBroadcastOperations().sendEvent(config.getCallbackEndpoint(), data);
            return;
        }
        SocketIOClient excludeClient = ioSocketServer.getServer()
                .getClient(UUID.fromString(excludeUser.getUuid()));
        if (excludeClient == null) {
            broadcast(config, data);
            return;
        }
        ioSocketServer.getServer()
                .getBroadcastOperations()
                .sendEvent(config.getCallbackEndpoint(), excludeClient, data);
    }

    public <T,R> void broadcast(SocketApiConfig<T,R> config, R data) {
        ioSocketServer.getServer().getBroadcastOperations().sendEvent(config.getCallbackEndpoint(), data);
    }
}
