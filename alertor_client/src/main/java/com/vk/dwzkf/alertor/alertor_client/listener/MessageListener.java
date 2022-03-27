package com.vk.dwzkf.alertor.alertor_client.listener;

import com.vk.dwzkf.alertor.commons.socket_api.message.UserMessage;

public interface MessageListener {
    void onMessage(UserMessage userMessage);
}
