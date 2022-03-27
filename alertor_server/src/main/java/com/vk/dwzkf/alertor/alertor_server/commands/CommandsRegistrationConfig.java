package com.vk.dwzkf.alertor.alertor_server.commands;

import com.vk.dwzkf.alertor.socket_server_core.command.CommandRegistry;
import com.vk.dwzkf.alertor.socket_server_core.command.SocketCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig.*;

@Configuration
@RequiredArgsConstructor
public class CommandsRegistrationConfig {
    private final CommandRegistry commandRegistry;

    static {
        CommandRegistry.register(ALERT_CONFIG, AlertCommand.class);
        CommandRegistry.register(USERS_STATE, GetUsersCommand.class);
    }

    @Autowired(required = false)
    public void registerAllBeans(List<SocketCommand<?, ?>> commands) {
        commands.forEach(commandRegistry::register);
    }
}
