package com.vk.dwzkf.alertor.alertor_client.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleConnectorListener implements ConnectorListener {
    @Override
    public void onConnect(Object[] args) {
        log.info("Connected to socket. {}", args);
    }

    @Override
    public void onDisconnect(Object[] args) {
        log.info("Disconnected from socket. {}", args);
    }

    @Override
    public void onConnectError(Object[] args) {
        log.warn("Connect error. {}", args);
    }
}
