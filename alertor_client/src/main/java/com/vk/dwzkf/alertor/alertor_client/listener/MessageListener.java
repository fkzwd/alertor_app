package com.vk.dwzkf.alertor.alertor_client.listener;

import com.vk.dwzkf.alertor.commons.socket_api.message.UserMessage;
import com.vk.dwzkf.alertor.commons.socket_api.message.UserMessageCallback;

public interface MessageListener {
    void onMessage(UserMessageCallback userMessage);
}
