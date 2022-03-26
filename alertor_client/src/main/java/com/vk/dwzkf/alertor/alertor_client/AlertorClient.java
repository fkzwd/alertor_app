package com.vk.dwzkf.alertor.alertor_client;

import com.vk.dwzkf.alertor.alertor_client.ui.ClientUi;
import com.vk.dwzkf.alertor.alertor_client_core.client.EventSender;
import com.vk.dwzkf.alertor.alertor_client_core.client.SocketClient;
import com.vk.dwzkf.alertor.alertor_client.config.EventHandlerConfig;
import com.vk.dwzkf.alertor.ui_core.UiCore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.locks.LockSupport;

@SpringBootApplication(scanBasePackages = "com.vk.dwzkf.alertor")
@RequiredArgsConstructor
public class AlertorClient {
    private final SocketClient socketClient;
    private final EventSender eventSender;

    public static void main(String[] args) throws Exception {
        EventHandlerConfig.configure();
        SpringApplicationBuilder builder = new SpringApplicationBuilder(AlertorClient.class);
        builder.headless(false);
        builder.run(args)
                .getBean(AlertorClient.class)
                .start();

    }

    public void start() {
        socketClient.run();
        new ClientUi(eventSender,socketClient).start();
    }
}
