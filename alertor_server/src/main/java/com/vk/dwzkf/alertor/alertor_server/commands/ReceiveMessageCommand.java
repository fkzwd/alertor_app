package com.vk.dwzkf.alertor.alertor_server.commands;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import com.vk.dwzkf.alertor.commons.socket_api.message.UserMessage;
import com.vk.dwzkf.alertor.socket_server_core.command.SocketCommand;
import com.vk.dwzkf.alertor.socket_server_core.config.EventSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.beans.EventHandler;

@Component
@RequiredArgsConstructor
public class ReceiveMessageCommand extends SocketCommand<UserMessage, UserMessage> {
    private final EventSender eventSender;

    @Override
    public UserMessage onEvent(UserData user, UserMessage arg) {
        eventSender.broadcast(user, SocketApiConfig.SEND_MESSAGE, new UserMessage(arg.getMessage(), user, false));
        return new UserMessage(arg.getMessage(), user, true);
    }
}
