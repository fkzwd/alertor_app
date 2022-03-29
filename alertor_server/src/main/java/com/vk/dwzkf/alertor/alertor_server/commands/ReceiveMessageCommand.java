package com.vk.dwzkf.alertor.alertor_server.commands;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import com.vk.dwzkf.alertor.commons.socket_api.message.UserMessage;
import com.vk.dwzkf.alertor.commons.socket_api.message.UserMessageCallback;
import com.vk.dwzkf.alertor.socket_server_core.command.SocketCommand;
import com.vk.dwzkf.alertor.socket_server_core.config.EventSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.beans.EventHandler;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReceiveMessageCommand extends SocketCommand<UserMessage, UserMessageCallback> {
    private final EventSender eventSender;

    @Override
    public UserMessageCallback onEvent(UserData user, UserMessage arg) {
        LocalDateTime time = LocalDateTime.now();
        eventSender.broadcast(user, SocketApiConfig.SEND_MESSAGE,
                UserMessageCallback.builder()
                        .message(arg.getMessage())
                        .userData(user)
                        .yourMessage(false)
                        .time(time)
                        .build()
        );
        return UserMessageCallback.builder()
                .message(arg.getMessage())
                .yourMessage(true)
                .userData(user)
                .time(time)
                .build();
    }
}
