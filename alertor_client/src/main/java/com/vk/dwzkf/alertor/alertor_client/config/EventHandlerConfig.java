package com.vk.dwzkf.alertor.alertor_client.config;

import com.vk.dwzkf.alertor.alertor_client.handlers.GetUsersCallbackHandler;
import com.vk.dwzkf.alertor.alertor_client.handlers.MessageHandler;
import com.vk.dwzkf.alertor.alertor_client.handlers.UserStateHandler;
import com.vk.dwzkf.alertor.alertor_client_core.config.EventHandlerRegistry;
import com.vk.dwzkf.alertor.alertor_client.handlers.AlertEventHandler;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;

import static com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig.*;

public class EventHandlerConfig {
    public static void configure() {
        EventHandlerRegistry.configureHandler(
                ALERT_CONFIG,
                AlertEventHandler.class
        );

        EventHandlerRegistry.configureHandler(
                USERS_STATE,
                GetUsersCallbackHandler.class
        );

        EventHandlerRegistry.configureHandler(
                USER_CONNECT_STATE,
                UserStateHandler.class
        );

        EventHandlerRegistry.configureHandler(
                SEND_MESSAGE,
                MessageHandler.class
        );
    }
}
