package com.vk.dwzkf.alertor.socket_server_core.config;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.listener.EventInterceptor;
import com.corundumstudio.socketio.listener.ExceptionListenerAdapter;
import com.corundumstudio.socketio.protocol.JacksonJsonSupport;
import com.fasterxml.jackson.databind.Module;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class IOSocketServer {
    private final SocketIOServer server;
    private final ConnectListener connectListener;
    private final DisconnectListener disconnectListener;

    public IOSocketServer(SocketServerConfig socketServerConfig,
                            ConnectListener connectListener,
                            ExceptionListenerAdapter exceptionHandler,
                            DisconnectListener disconnectListener,
                            AuthorizationListener authorizationListener) {
        Configuration config = new Configuration();
        config.setPort(socketServerConfig.getPort());
        config.setExceptionListener(exceptionHandler);
        config.setPingInterval(30000);
        config.setContext(socketServerConfig.getContext());
        config.setPingTimeout(15000);
        config.setRandomSession(true);
        config.setTransports(Transport.WEBSOCKET);
        config.setAuthorizationListener(authorizationListener);
        setJacksonTimeModule(config);
        server = new SocketIOServer(config);
        this.connectListener = connectListener;
        this.disconnectListener = disconnectListener;
    }

    private void setJacksonTimeModule(Configuration config) {
        JacksonJsonSupport jacksonJsonSupport = new JacksonJsonSupport(ObjectMapperConfigurator.getModules().toArray(Module[]::new));
        config.setJsonSupport(jacksonJsonSupport);
    }

    @PostConstruct
    public void init() {
        server.addConnectListener(connectListener);
        server.addDisconnectListener(disconnectListener);
        startSocketServer();
    }

    private void startSocketServer() {
        try {
            server.start();
        } catch (Exception e) {
            log.error("Could not start socket server: {}", e.getMessage(), e);
            System.exit(-1);
        }
    }

    @PreDestroy
    public void stop() {
        log.info("stopping server socket");
        server.stop();
    }

    public SocketIOServer getServer() {
        return server;
    }
}
