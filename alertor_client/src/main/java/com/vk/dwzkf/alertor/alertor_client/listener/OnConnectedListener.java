package com.vk.dwzkf.alertor.alertor_client.listener;

import com.vk.dwzkf.alertor.alertor_client_core.client.EventSender;
import com.vk.dwzkf.alertor.alertor_client_core.listener.SocketConnectorListener;
import com.vk.dwzkf.alertor.commons.socket_api.AlertEvent;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnConnectedListener implements SocketConnectorListener {
    private final EventSender eventSender;

    @Override
    public void onConnect(Object[] args) {

    }

    @Override
    public void onDisconnect(Object[] args) {

    }

    @Override
    public void onConnectError(Object[] args) {

    }
}
