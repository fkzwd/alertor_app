package com.vk.dwzkf.alertor.alertor_server.listeners;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import com.vk.dwzkf.alertor.commons.socket_api.AlertCallback;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import com.vk.dwzkf.alertor.socket_server_core.config.EventSender;
import com.vk.dwzkf.alertor.socket_server_core.monitoring.UsersListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnUserConnectListener implements UsersListener {
    private final EventSender eventSender;

    @Override
    public void onConnect(UserData userData) {
        eventSender.sendEvent(userData,
                SocketApiConfig.ALERT_CONFIG,
                new AlertCallback("Go KURIT!! ", 150 ,30)
        );
    }

    @Override
    public void onDisconnect(UserData userData) {

    }
}
