package com.vk.dwzkf.alertor.alertor_server.config;

import com.corundumstudio.socketio.SocketIOClient;
import com.vk.dwzkf.alertor.commons.entity.UserData;
import com.vk.dwzkf.alertor.socket_server_core.extractors.UserDataExtractor;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Random;

@Component
public class UserDataExtractorConfiguration implements UserDataExtractor<SocketIOClient> {
    private final Random random = new Random();

    @Override
    public UserData extract(SocketIOClient from) {
        String name = from.getHandshakeData().getSingleUrlParam("name");
        return UserData.builder()
                .name(name == null ? from.getRemoteAddress().toString() : name)
                .uuid(from.getSessionId().toString())
                .color(randomColor(from))
                .build();
    }

    private int randomColor(SocketIOClient client) {
        if (client.getHandshakeData().getSingleUrlParam("color") != null) {
            try {
                return Integer.parseInt(client.getHandshakeData().getSingleUrlParam("color"));
            } catch (Exception e) {
                return randomColor();
            }
        }
        if (client.get("color") == null) {
            client.set("color", randomColor());
        }
        return client.get("color");
    }

    private int randomColor() {
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(2000) + 1000) / 10000f;
        final float luminance = 0.9f;
        return Color.getHSBColor(hue, saturation, luminance).getRGB();
    }
}
