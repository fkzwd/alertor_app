package com.vk.dwzkf.alertor.socket_server_core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties("socket.server")
public class SocketServerConfig {
    private int port;
    private String context;
}
