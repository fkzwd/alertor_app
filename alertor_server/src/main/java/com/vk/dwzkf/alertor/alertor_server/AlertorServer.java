package com.vk.dwzkf.alertor.alertor_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.vk.dwzkf.alertor")
public class AlertorServer {
    public static void main(String[] args) {
        SpringApplication.run(AlertorServer.class);
    }
}
