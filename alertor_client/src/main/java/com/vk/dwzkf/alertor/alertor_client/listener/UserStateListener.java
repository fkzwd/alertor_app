package com.vk.dwzkf.alertor.alertor_client.listener;

import com.vk.dwzkf.alertor.commons.socket_api.users_state.UserConnectData;

public interface UserStateListener {
    void onConnected(UserConnectData userData);
    void onDisconnected(UserConnectData userData);
}
