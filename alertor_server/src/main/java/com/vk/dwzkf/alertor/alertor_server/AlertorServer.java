package com.vk.dwzkf.alertor.alertor_server;

import com.vk.dwzkf.alertor.alertor_server.ui.AlertorServerUi;
import com.vk.dwzkf.alertor.alertor_server.ui.UiConfigurator;
import com.vk.dwzkf.alertor.socket_server_core.config.EventSender;
import com.vk.dwzkf.alertor.socket_server_core.config.IOSocketServer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication(scanBasePackages = "com.vk.dwzkf.alertor")
@RequiredArgsConstructor
public class AlertorServer {
    public static boolean console = true;
    private final UiConfigurator uiConfigurator;
    private final IOSocketServer socketServer;

    public static void main(String[] args) {
        if (args.length > 0) console = false;
        SpringApplication.run(AlertorServer.class)
                .getBean(AlertorServer.class).run();
    }

    public void run() {
        socketServer.start();
        if (console) {
            uiConfigurator.configure(new AlertorServerUi(socketServer)).start();
        }
    }
}
