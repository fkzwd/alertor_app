package com.vk.dwzkf.alertor.alertor_client.handlers;

import com.vk.dwzkf.alertor.alertor_client.listener.AlertListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlertListenersConfig {
    private final AlertEventHandler alertEventHandler;

    @Autowired
    public void addListeners(List<AlertListener> listeners) {
        listeners.forEach(alertEventHandler::addListener);
    }
}
