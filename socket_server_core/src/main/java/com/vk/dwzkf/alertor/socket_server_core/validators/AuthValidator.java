package com.vk.dwzkf.alertor.socket_server_core.validators;

import com.corundumstudio.socketio.HandshakeData;

public interface AuthValidator {
    boolean validate(HandshakeData handshakeData);
}
