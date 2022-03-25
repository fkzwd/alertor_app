package com.vk.dwzkf.alertor.alertor_client;

import com.vk.dwzkf.alertor.alertor_client.client.SocketClient;
import com.vk.dwzkf.alertor.alertor_client.config.EventHandlerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@Slf4j
public class AlertorClient {
    public static void main(String[] args) {
        EventHandlerConfig.configure();
        SpringApplicationBuilder builder = new SpringApplicationBuilder(AlertorClient.class);
        builder.headless(false);
        SocketClient socket = builder.run(args)
                .getBean(SocketClient.class);
        socket.run();
    }
}
