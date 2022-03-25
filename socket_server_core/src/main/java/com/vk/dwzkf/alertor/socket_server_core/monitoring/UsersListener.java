package com.vk.dwzkf.alertor.socket_server_core.monitoring;

import com.vk.dwzkf.alertor.commons.entity.UserData;

public interface UsersListener {
    void onConnect(UserData userData);
    void onDisconnect(UserData userData);
}
