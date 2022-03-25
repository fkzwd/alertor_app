package com.vk.dwzkf.alertor.socket_server_core.listeners;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.vk.dwzkf.alertor.socket_server_core.extractors.UserDataExtractor;
import com.vk.dwzkf.alertor.socket_server_core.monitoring.UserConnectionMonitoring;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnConnectListener implements ConnectListener {
    private final UserConnectionMonitoring userMonitoring;
    private final UserDataExtractor<SocketIOClient> userDataExtractor;

    @Override
    public void onConnect(SocketIOClient client) {
        log.info("User connected: "+client.getRemoteAddress());
        log.info("Handshake: {}", client.getHandshakeData().getUrlParams());
        userMonitoring.onConnect(userDataExtractor.extract(client));
    }
}
