package com.vk.dwzkf.alertor.alertor_server.config;

import com.corundumstudio.socketio.SocketIOClient;
import com.vk.dwzkf.alertor.commons.entity.UserData;
import com.vk.dwzkf.alertor.socket_server_core.extractors.UserDataExtractor;
import org.springframework.stereotype.Component;

@Component
public class UserDataExtractorConfiguration implements UserDataExtractor<SocketIOClient> {
    @Override
    public UserData extract(SocketIOClient from) {
        final String name = from.getHandshakeData().getSingleUrlParam("name");
        return UserData.builder()
                .name(name == null ? from.getRemoteAddress().toString() : name)
                .uuid(from.getSessionId().toString())
                .build();
    }
}
