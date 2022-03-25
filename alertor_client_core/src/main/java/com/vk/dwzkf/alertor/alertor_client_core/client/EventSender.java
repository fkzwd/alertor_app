package com.vk.dwzkf.alertor.alertor_client_core.client;

import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventSender {
    private final SocketClient socketClient;
//    private final ObjectMapper objectMapper = ObjectMapperConfigurator.configure(new ObjectMapper());

    public <T,R> void emit(SocketApiConfig<T,R> config, T data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            socketClient.getSocket()
                    .emit(config.getEventEndpoint(), jsonObject);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
