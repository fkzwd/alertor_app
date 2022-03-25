package com.vk.dwzkf.alertor.socket_server_core.monitoring;

import com.vk.dwzkf.alertor.commons.entity.UserData;

import java.util.List;

public interface UserConnectionMonitoring {
    void onConnect(UserData userData);
    void onDisconnect(UserData userData);
    void addUsersListener(UsersListener usersListener);
    List<UserMonitoringData> getCurrentState();
}
