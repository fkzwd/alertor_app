package com.vk.dwzkf.alertor.commons.socket_api;

import com.vk.dwzkf.alertor.commons.socket_api.get_users.GetUsersCallback;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SocketApiConfig<T, R> {
    public static final SocketApiConfig<AlertEvent, AlertCallback> ALERT_CONFIG;
    public static final SocketApiConfig<Void, GetUsersCallback> GET_USERS;

    static {
        ALERT_CONFIG = new SocketApiConfig<>(
                "alert",
                "alertCallback",
                AlertEvent.class,
                AlertCallback.class);

        GET_USERS = new SocketApiConfig<>(
                "getUsers",
                "getUsersCallback",
                Void.class,
                GetUsersCallback.class
        );
    }

    private final String eventEndpoint;
    private final String callbackEndpoint;
    private final Class<T> requestClass;
    private final Class<R> responseClass;
}
