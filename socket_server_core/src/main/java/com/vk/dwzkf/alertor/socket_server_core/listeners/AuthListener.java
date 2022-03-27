package com.vk.dwzkf.alertor.socket_server_core.listeners;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import com.vk.dwzkf.alertor.socket_server_core.validators.AuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthListener implements AuthorizationListener {
    private final List<AuthValidator> validators = new ArrayList<>();

    @Autowired(required = false)
    public void configureValidators(List<AuthValidator> validators) {
        this.validators.addAll(validators);
    }

    @Override
    public boolean isAuthorized(HandshakeData data) {
        for (AuthValidator validator : validators) {
            if (!validator.validate(data)) {
                return false;
            }
        }
        return true;
    }
}
