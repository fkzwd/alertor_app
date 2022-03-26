package com.vk.dwzkf.alertor.alertor_server.commands;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import com.vk.dwzkf.alertor.commons.socket_api.get_users.GetUsersCallback;
import com.vk.dwzkf.alertor.socket_server_core.command.SocketCommand;
import com.vk.dwzkf.alertor.socket_server_core.monitoring.UserMonitoring;
import com.vk.dwzkf.alertor.socket_server_core.monitoring.UserMonitoringData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetUsersCommand extends SocketCommand<Void, GetUsersCallback> {
    private final UserMonitoring userMonitoring;

    @Override
    public GetUsersCallback onEvent(UserData user, Void arg) {
        return new GetUsersCallback(
                userMonitoring.getConnectedUsers()
                        .stream()
                        .map(UserMonitoringData::getUserData)
                        .collect(Collectors.toList())
        );
    }
}
