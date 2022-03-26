package com.vk.dwzkf.alertor.alertor_server.ui.commands;

import com.vk.dwzkf.alertor.alertor_server.ui.ServerUiCommand;
import com.vk.dwzkf.alertor.socket_server_core.config.IOSocketServer;
import com.vk.dwzkf.alertor.socket_server_core.monitoring.UserMonitoring;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestartServerCommand extends ServerUiCommand {
    private final IOSocketServer ioSocketServer;
    private final UserMonitoring userMonitoring;

    @Override
    public void execute() {
        ioSocketServer.restart();
        userMonitoring.clear();
    }

    @Override
    public String name() {
        return "Restart server";
    }
}
