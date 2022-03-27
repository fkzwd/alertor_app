package com.vk.dwzkf.alertor.alertor_client.handlers;

import com.vk.dwzkf.alertor.alertor_client.listener.UserStateListener;
import com.vk.dwzkf.alertor.alertor_client_core.handlers.EventHandler;
import com.vk.dwzkf.alertor.commons.socket_api.users_state.UserConnectCallback;
import com.vk.dwzkf.alertor.commons.socket_api.users_state.UserConnectData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserStateHandler extends EventHandler<UserConnectCallback> {
    private final List<UserStateListener> userStateListeners = new ArrayList<>();

    @Autowired(required = false)
    public void configureListeners(List<UserStateListener> listeners) {
        userStateListeners.addAll(listeners);
    }

    @Override
    public void handleEvent(UserConnectCallback event) {
        for (UserConnectData connectState : event.getConnectStates()) {
            if (connectState.isConnected()) {
                userStateListeners.forEach(l -> {
                    try {
                        l.onConnected(connectState.getUserData());
                    } catch (Exception e) {
                        log.error("Error on listener {}", l.getClass().getSimpleName(), e);
                    }
                });
            } else {
                userStateListeners.forEach(l -> {
                    try {
                        l.onDisconnected(connectState.getUserData());
                    } catch (Exception e) {
                        log.error("Error on listener {}", l.getClass().getSimpleName(), e);
                    }
                });
            }
        }
    }
}
