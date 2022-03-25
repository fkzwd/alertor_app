package com.vk.dwzkf.alertor.alertor_client;

import com.vk.dwzkf.alertor.alertor_client_core.client.SocketClient;
import com.vk.dwzkf.alertor.alertor_client.config.EventHandlerConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = "com.vk.dwzkf.alertor")
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
