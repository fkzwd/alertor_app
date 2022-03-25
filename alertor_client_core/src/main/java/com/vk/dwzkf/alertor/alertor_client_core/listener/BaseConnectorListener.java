package com.vk.dwzkf.alertor.alertor_client_core.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BaseConnectorListener implements ConnectorListener {
    private final List<SocketConnectorListener> listeners = Collections.synchronizedList(new ArrayList<>());

    public void registerListener(SocketConnectorListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            log.info("Registered connector listener: {}", listener.getClass().getSimpleName());
        } else {
            log.warn("Listener[{}] already registered.", listener.getClass().getSimpleName());
        }
    }


    @Override
    public void onConnect(Object[] args) {
        log.info("Connected to socket. {}", args);
        listeners.forEach(l -> {
            try {
                l.onConnect(args);
            } catch (Exception e) {
                log.error("Error onConnect on listener: {}", l.getClass().getSimpleName());
            }
        });
    }

    @Override
    public void onDisconnect(Object[] args) {
        log.info("Disconnected from socket. {}", args);
        listeners.forEach(l -> {
            try {
                l.onDisconnect(args);
            } catch (Exception e) {
                log.error("Error onDisconnect on listener: {}", l.getClass().getSimpleName());
            }
        });
    }

    @Override
    public void onConnectError(Object[] args) {
        log.warn("Connect error. {}", args);
        listeners.forEach(l -> {
            try {
                l.onConnectError(args);
            } catch (Exception e) {
                log.error("Error onConnectError on listener: {}", l.getClass().getSimpleName());
            }
        });
    }
}
