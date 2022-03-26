package com.vk.dwzkf.alertor.alertor_server.ui.commands;

import com.vk.dwzkf.alertor.alertor_server.ui.ServerUiCommand;
import com.vk.dwzkf.alertor.socket_server_core.monitoring.UserMonitoring;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ShowClientsCommand extends ServerUiCommand {
    private final UserMonitoring userMonitoring;

    @Override
    public void execute() {
        System.out.println("-------Users--------");
        userMonitoring.getCurrentState()
                .forEach(m -> {
                    System.out.println(
                            String.format(
                                    "\t User:%s, Connected: %s, Connect-time:%s, Disconnect-time:%s",
                                    m.getUserData().getName(),
                                    m.isConnected(),
                                    m.getConnectTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                                    m.getDisconnectTime() == null ? "" : m.getDisconnectTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                            )
                    );
                });
    }

    @Override
    public String name() {
        return "Show user state";
    }
}
