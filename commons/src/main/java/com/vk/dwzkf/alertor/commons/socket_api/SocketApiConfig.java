package com.vk.dwzkf.alertor.commons.socket_api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SocketApiConfig<T, R> {
    public static final SocketApiConfig<AlertEvent, AlertCallback> ALERT_CONFIG;

    static {
        ALERT_CONFIG = new SocketApiConfig<>(
                "alert",
                "alertCallback",
                AlertEvent.class,
                AlertCallback.class);

    }

    private final String eventEndpoint;
    private final String callbackEndpoint;
    private final Class<T> requestClass;
    private final Class<R> responseClass;
}
