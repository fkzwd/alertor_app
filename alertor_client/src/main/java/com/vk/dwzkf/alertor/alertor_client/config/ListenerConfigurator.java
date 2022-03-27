package com.vk.dwzkf.alertor.alertor_client.config;

import com.vk.dwzkf.alertor.alertor_client.handlers.MessageHandler;
import com.vk.dwzkf.alertor.alertor_client.listener.MessageListener;
import com.vk.dwzkf.alertor.alertor_client_core.listener.ConnectorListener;
import com.vk.dwzkf.alertor.alertor_client_core.listener.SocketConnectorListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListenerConfigurator {
    private final ConnectorListener connectorListener;
    private final MessageHandler messageHandler;

    @Autowired(required = false)
    public void listeners(List<SocketConnectorListener> listeners) {
        listeners.forEach(connectorListener::registerListener);
    }

    @Autowired(required = false)
    public void addMessageListeners(List<MessageListener> listeners) {
        listeners.forEach(messageHandler::addListener);
    }
}
