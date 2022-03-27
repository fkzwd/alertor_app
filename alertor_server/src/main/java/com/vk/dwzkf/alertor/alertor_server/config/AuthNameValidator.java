package com.vk.dwzkf.alertor.alertor_server.config;

import com.corundumstudio.socketio.HandshakeData;
import com.vk.dwzkf.alertor.commons.configurators.Constants;
import com.vk.dwzkf.alertor.socket_server_core.validators.AuthValidator;
import org.springframework.stereotype.Component;

@Component
public class AuthNameValidator implements AuthValidator {
    @Override
    public boolean validate(HandshakeData handshakeData) {
        return handshakeData.getSingleUrlParam("name") != null && handshakeData.getSingleUrlParam("name").matches(Constants.SOCKET_NAME_REGEX);
    }
}
