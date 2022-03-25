package com.vk.dwzkf.alertor.alertor_client.config;

import com.vk.dwzkf.alertor.alertor_client.handlers.AlertEventHandler;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;

import static com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig.ALERT_CONFIG;

public class EventHandlerConfig {
    public static void configure() {
        EventHandlerRegistry.configureHandler(
                ALERT_CONFIG,
                AlertEventHandler.class
        );
    }
}
