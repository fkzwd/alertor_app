package com.vk.dwzkf.alertor.alertor_client.listener;

import com.vk.dwzkf.alertor.commons.entity.UserData;

public interface UserStateListener {
    void onConnected(UserData userData);
    void onDisconnected(UserData userData);
}
