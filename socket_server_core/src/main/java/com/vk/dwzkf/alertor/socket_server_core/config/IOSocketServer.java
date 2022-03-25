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
import com.vk.dwzkf.alertor.socket_server_core.command.CommandRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class IOSocketServer {
    private SocketIOServer server;
    private final CommandRegistry commandRegistry;
    private final ConnectListener connectListener;
    private final DisconnectListener disconnectListener;
    private final Configuration configuration;

    public IOSocketServer(SocketServerConfig socketServerConfig,
                            ConnectListener connectListener,
                            ExceptionListenerAdapter exceptionHandler,
                            DisconnectListener disconnectListener,
                            AuthorizationListener authorizationListener,
                          CommandRegistry commandRegistry) {
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
        this.configuration = config;
        this.connectListener = connectListener;
        this.disconnectListener = disconnectListener;
        this.commandRegistry = commandRegistry;
    }

    private void setJacksonTimeModule(Configuration config) {
        JacksonJsonSupport jacksonJsonSupport = new JacksonJsonSupport(ObjectMapperConfigurator.getModules().toArray(Module[]::new));
        config.setJsonSupport(jacksonJsonSupport);
    }

    public void start() {
        try {
            if (server != null) {
                stop();
            }
            server = new SocketIOServer(configuration);
            commandRegistry.configure(server);
            server.addConnectListener(connectListener);
            server.addDisconnectListener(disconnectListener);
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
        server = null;
    }

    public SocketIOServer getServer() {
        return server;
    }
}
