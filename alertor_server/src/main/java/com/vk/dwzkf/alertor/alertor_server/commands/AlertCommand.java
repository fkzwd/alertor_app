package com.vk.dwzkf.alertor.alertor_server.commands;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import com.vk.dwzkf.alertor.commons.socket_api.AlertEvent;
import com.vk.dwzkf.alertor.commons.socket_api.AlertCallback;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import com.vk.dwzkf.alertor.socket_server_core.command.SocketCommand;
import com.vk.dwzkf.alertor.socket_server_core.config.EventSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertCommand extends SocketCommand<AlertEvent, AlertCallback> {
    private static final int defaultTimeout = 150;
    private static final int defaultCycles = 30;
    private static final String defaultMessage = "GO SMOKE!! ";

    private final EventSender eventSender;

    @Override
    public AlertCallback onEvent(UserData user, AlertEvent arg) {
        int timeout = arg.getTimeout() == null ? defaultTimeout : arg.getTimeout();
        int cycles = arg.getCycles() == null ? defaultCycles : arg.getCycles();
        String message = arg.getMessage() == null ? defaultMessage : arg.getMessage();

        AlertCallback alertCallback = new AlertCallback(arg.getMessage(),
                timeout,
                cycles,
                user
        );
        eventSender.broadcast(user, SocketApiConfig.ALERT_CONFIG, alertCallback);
        return new AlertCallback(message, timeout, cycles, user);
    }
}
