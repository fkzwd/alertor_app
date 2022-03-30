package com.vk.dwzkf.alertor.alertor_client;

import com.vk.dwzkf.alertor.alertor_client.alertor.AudioAlertor;
import com.vk.dwzkf.alertor.alertor_client.ui.ClientUi;
import com.vk.dwzkf.alertor.alertor_client.ui.window.MainUi;
import com.vk.dwzkf.alertor.alertor_client_core.client.EventSender;
import com.vk.dwzkf.alertor.alertor_client_core.client.SocketClient;
import com.vk.dwzkf.alertor.alertor_client.config.EventHandlerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = "com.vk.dwzkf.alertor")
@RequiredArgsConstructor
public class AlertorClient {
    private final SocketClient socketClient;
    private final EventSender eventSender;
    private final MainUi mainUi;
    private static String mode = "window";

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("window")) {
                mode = "window";
            } else if (args[0].equalsIgnoreCase("console")) {
                mode = "console";
            }
        }
        EventHandlerConfig.configure();
        SpringApplicationBuilder builder = new SpringApplicationBuilder(AlertorClient.class);
        builder.headless(false);
        builder.run(args)
                .getBean(AlertorClient.class)
                .start();

    }

    public void start() {
//        socketClient.connect();
        if (mode.equalsIgnoreCase("console")) {
            new ClientUi(eventSender,socketClient).start();
        } else if (mode.equalsIgnoreCase("window")) {
            mainUi.start();
        } else {
            System.exit(0);
        }
    }
}
