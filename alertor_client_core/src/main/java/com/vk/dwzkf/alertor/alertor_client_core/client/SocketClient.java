package com.vk.dwzkf.alertor.alertor_client_core.client;

import com.vk.dwzkf.alertor.alertor_client_core.config.EventHandlerRegistry;
import com.vk.dwzkf.alertor.alertor_client_core.config.SocketConfig;
import com.vk.dwzkf.alertor.alertor_client_core.listener.ConnectorListener;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketClient {
    @Getter
    private final SocketConfig socketConfig;
    private final ConnectorListener connectorListener;
    private final EventHandlerRegistry eventHandlerRegistry;
    @Getter
    private Socket socket;
    @Getter
    private boolean initialized = false;
    @Getter
    private IO.Options config;

    @PostConstruct
    public void initSocket() {
        if (initialized) return;
        URI uri = URI.create(String.format("%s://%s:%s", socketConfig.getProtocol(), socketConfig.getHost(), socketConfig.getPort()));
        config = getOptions();
        Socket socket = IO.socket(uri, config);
        socket.on(Socket.EVENT_CONNECT, connectorListener::onConnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, connectorListener::onConnectError);
        socket.on(Socket.EVENT_DISCONNECT, connectorListener::onDisconnect);
        eventHandlerRegistry.configure(socket);
        this.socket = socket;
        initialized = true;
    }

    public void restart() {
        disconnect();
        initSocket();
        connect();
    }

    public void connect() {
        if (initialized)
            socket.connect();
    }

    public void disconnect() {
        if (initialized) {
            socket.disconnect();
            socket.close();
        }
        initialized = false;
        socket = null;
    }

    private IO.Options getOptions() {
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.transports = new String[] {WebSocket.NAME};
        options.reconnection = true;
        options.port = socketConfig.getPort();
        options.path = socketConfig.getContext();
        return options;
    }
}
