package com.vk.dwzkf.alertor.alertor_client.config;

import com.vk.dwzkf.alertor.alertor_client.handlers.GetUsersCallbackHandler;
import com.vk.dwzkf.alertor.alertor_client_core.config.EventHandlerRegistry;
import com.vk.dwzkf.alertor.alertor_client.handlers.AlertEventHandler;

import static com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig.ALERT_CONFIG;
import static com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig.GET_USERS;

public class EventHandlerConfig {
    public static void configure() {
        EventHandlerRegistry.configureHandler(
                ALERT_CONFIG,
                AlertEventHandler.class
        );

        EventHandlerRegistry.configureHandler(
                GET_USERS,
                GetUsersCallbackHandler.class
        );
    }
}
