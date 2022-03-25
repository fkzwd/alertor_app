package com.vk.dwzkf.alertor.socket_server_core.listeners;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import org.springframework.stereotype.Component;

@Component
public class AuthListener implements AuthorizationListener {
    @Override
    public boolean isAuthorized(HandshakeData data) {
        return true;
    }
}
