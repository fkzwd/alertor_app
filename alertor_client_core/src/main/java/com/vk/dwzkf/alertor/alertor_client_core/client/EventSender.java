package com.vk.dwzkf.alertor.alertor_client_core.client;

import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventSender {
    private final SocketClient socketClient;

    public <T, R> void emit(SocketApiConfig<T, R> config, T data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            socketClient.getSocket()
                    .emit(config.getEventEndpoint(), jsonObject);
        } catch (Exception exception) {
            log.error("Error on emitting: endpoint[{}], data[{}]",
                    config.getEventEndpoint(),
                    data
            );
        }
    }

    public <T, R> void emit(SocketApiConfig<T, R> config) {
        try {
            socketClient.getSocket().emit(config.getEventEndpoint());
        } catch (Exception e) {
            log.error("Error on emitting: endpoint[{}], data[]",
                    config.getEventEndpoint()
            );
        }
    }
}
