package com.vk.dwzkf.alertor.alertor_server.commands;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import com.vk.dwzkf.alertor.commons.socket_api.message.UserMessage;
import com.vk.dwzkf.alertor.commons.socket_api.message.UserMessageCallback;
import com.vk.dwzkf.alertor.commons.utils.DateTimeUtils;
import com.vk.dwzkf.alertor.socket_server_core.command.SocketCommand;
import com.vk.dwzkf.alertor.socket_server_core.config.EventSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.beans.EventHandler;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class ReceiveMessageCommand extends SocketCommand<UserMessage, UserMessageCallback> {
    private final EventSender eventSender;

    @Override
    public UserMessageCallback onEvent(UserData user, UserMessage arg) {
        OffsetDateTime time = LocalDateTime.now().atOffset(ZoneOffset.of("+03:00"));
        eventSender.broadcast(user, SocketApiConfig.SEND_MESSAGE,
                UserMessageCallback.builder()
                        .message(arg.getMessage())
                        .userData(user)
                        .yourMessage(false)
                        .time(time.format(DateTimeUtils.DATE_TIME_FORMATTER))
                        .build()
        );
        return UserMessageCallback.builder()
                .message(arg.getMessage())
                .yourMessage(true)
                .userData(user)
                .time(time.format(DateTimeUtils.DATE_TIME_FORMATTER))
                .build();
    }
}
