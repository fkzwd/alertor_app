package com.vk.dwzkf.alertor.alertor_client_core;

import com.vk.dwzkf.alertor.alertor_client_core.client.SocketClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@Slf4j
public class AlertorClient {
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(AlertorClient.class);
        builder.headless(false);
        SocketClient socket = builder.run(args)
                .getBean(SocketClient.class);
        socket.run();
    }
}
