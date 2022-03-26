package com.vk.dwzkf.alertor.alertor_server.config;

import com.vk.dwzkf.alertor.socket_server_core.monitoring.UserConnectionMonitoring;
import com.vk.dwzkf.alertor.socket_server_core.monitoring.UsersListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class UsersListenerConfiguration {
    private final UserConnectionMonitoring userMonitoring;

    @Autowired(required = false)
    public void registerListener(List<UsersListener> usersListeners) {
        usersListeners.forEach(userMonitoring::addUsersListener);
    }
}
