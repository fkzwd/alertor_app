package com.vk.dwzkf.alertor.alertor_client.handlers;

import com.vk.dwzkf.alertor.alertor_client.listener.MessageListener;
import com.vk.dwzkf.alertor.alertor_client_core.handlers.EventHandler;
import com.vk.dwzkf.alertor.commons.socket_api.message.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageHandler extends EventHandler<UserMessage> {
    private final List<MessageListener> listeners = new ArrayList<>();

    public void addListener(MessageListener messageListener) {
        listeners.add(messageListener);
    }

    @Override
    public void handleEvent(UserMessage event) {
        listeners.forEach(m -> {
            try {
                m.onMessage(event);
            } catch (Exception e) {
                log.error("Error on listener {}.", m.getClass().getSimpleName(), e);
            }
        });
    }
}
