package com.vk.dwzkf.alertor.alertor_server.listeners;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import com.vk.dwzkf.alertor.commons.socket_api.get_users.GetUsersCallback;
import com.vk.dwzkf.alertor.commons.socket_api.users_state.UserConnectCallback;
import com.vk.dwzkf.alertor.commons.socket_api.users_state.UserConnectData;
import com.vk.dwzkf.alertor.socket_server_core.config.EventSender;
import com.vk.dwzkf.alertor.socket_server_core.monitoring.UserMonitoring;
import com.vk.dwzkf.alertor.socket_server_core.monitoring.UserMonitoringData;
import com.vk.dwzkf.alertor.socket_server_core.monitoring.UsersListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OnUserConnectListener implements UsersListener {
    private final EventSender eventSender;
    private final UserMonitoring userMonitoring;

    @Override
    public void onConnect(UserData userData) {
        eventSender.sendEvent(userData, SocketApiConfig.USER_CONNECT_STATE,
                new UserConnectCallback(
                        userMonitoring.getConnectedUsers()
                                .stream()
                                .map(m -> new UserConnectData(true, m.getUserData()))
                                .collect(Collectors.toList())
                )
        );
        eventSender.broadcast(userData,
                SocketApiConfig.USER_CONNECT_STATE,
                new UserConnectCallback(List.of(new UserConnectData(true, userData))));
    }

    @Override
    public void onDisconnect(UserData userData) {
        eventSender.broadcast(userData,
                SocketApiConfig.USER_CONNECT_STATE,
                new UserConnectCallback(List.of(new UserConnectData(false, userData))));
    }
}
