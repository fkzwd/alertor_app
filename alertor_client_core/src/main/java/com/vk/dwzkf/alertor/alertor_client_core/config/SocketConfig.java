package com.vk.dwzkf.alertor.alertor_client_core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("socket.client")
@Getter
@Setter
public class SocketConfig {
    private String host = "192.168.101.207";
    private String protocol = "http";
    private int port = 20021;
    private String context = "/alertor";
}
