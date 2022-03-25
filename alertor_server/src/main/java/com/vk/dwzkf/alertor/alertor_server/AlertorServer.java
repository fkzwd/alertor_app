package com.vk.dwzkf.alertor.alertor_server;

import com.vk.dwzkf.alertor.socket_server_core.config.IOSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.vk.dwzkf.alertor")
public class AlertorServer {
    public static void main(String[] args) {
        SpringApplication.run(AlertorServer.class)
                .getBean(IOSocketServer.class)
                .start();
    }
}
