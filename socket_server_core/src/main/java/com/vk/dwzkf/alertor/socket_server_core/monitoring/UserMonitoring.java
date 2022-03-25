package com.vk.dwzkf.alertor.socket_server_core.monitoring;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserMonitoring implements UserConnectionMonitoring {
    private final Map<String, UserMonitoringData> userMap = new ConcurrentHashMap<>();
    private final List<UsersListener> listeners = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void addUsersListener(UsersListener usersListener) {
        if (listeners.contains(usersListener)) {
            log.info("Listener already added. {}", usersListener.getClass().getSimpleName());
        } else {
            listeners.add(usersListener);
        }
    }

    @Override
    public void onConnect(UserData userData) {
        UserMonitoringData connectedUser = userMap.computeIfAbsent(userData.getUuid(),
                (k) ->
                        UserMonitoringData.builder()
                                .userData(userData)
                                .connected(true)
                                .connectTime(LocalDateTime.now())
                                .build()
        );
        log.info("User connected: {}", connectedUser);
        listeners.forEach(l -> {
            try {
                l.onConnect(userData);
            } catch (Exception e) {
                log.warn("Error on listener {}.", l.getClass().getSimpleName(), e);
            }
        });
    }

    @Override
    public void onDisconnect(UserData userData) {
        UserMonitoringData disconnectedUser = userMap.getOrDefault(
                userData.getUuid(),
                UserMonitoringData.builder()
                        .userData(userData)
                        .build()
        );
        disconnectedUser.setDisconnectTime(LocalDateTime.now());
        disconnectedUser.setConnected(false);
        userMap.put(userData.getUuid(), disconnectedUser);
        log.info("User disconnected: {}", disconnectedUser);
        listeners.forEach(l -> {
            try {
                l.onDisconnect(userData);
            } catch (Exception e) {
                log.warn("Error on listener {}.", l.getClass().getSimpleName(), e);
            }
        });
    }

    @Override
    public List<UserMonitoringData> getCurrentState() {
        return new ArrayList<>(userMap.values());
    }
}
