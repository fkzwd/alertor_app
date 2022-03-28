package com.vk.dwzkf.alertor.alertor_client.handlers;

import com.vk.dwzkf.alertor.alertor_client.alertor.AlertConfig;
import com.vk.dwzkf.alertor.alertor_client_core.handlers.EventHandler;
import com.vk.dwzkf.alertor.alertor_client.listener.AlertListener;
import com.vk.dwzkf.alertor.commons.socket_api.AlertCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertEventHandler extends EventHandler<AlertCallback> {
    private final List<AlertListener> alertListeners = new ArrayList<>();

    @Autowired
    public void addListeners(List<AlertListener> listeners) {
        alertListeners.addAll(listeners);
    }

    @Override
    public void handleEvent(AlertCallback event) {
        alertListeners.forEach(l -> {
            try {
                l.onAlert(event);
            } catch (Exception e) {
                log.error("Error on alertor {}.", l.getClass().getSimpleName(), e);
            }
        });
    }
}
