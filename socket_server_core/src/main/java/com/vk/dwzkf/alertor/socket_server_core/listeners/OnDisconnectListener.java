package com.vk.dwzkf.alertor.socket_server_core.listeners;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.vk.dwzkf.alertor.socket_server_core.extractors.UserDataExtractor;
import com.vk.dwzkf.alertor.socket_server_core.monitoring.UserConnectionMonitoring;
import com.vk.dwzkf.alertor.socket_server_core.monitoring.UserMonitoring;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnDisconnectListener implements DisconnectListener {
    private final UserConnectionMonitoring userMonitoring;
    private final UserDataExtractor<SocketIOClient> userDataExtractor;


    @Override
    public void onDisconnect(SocketIOClient client) {
        log.info("User disconnected: "+client.getRemoteAddress());
        userMonitoring.onDisconnect(userDataExtractor.extract(client));
    }
}
