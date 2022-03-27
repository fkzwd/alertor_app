package com.vk.dwzkf.alertor.commons.socket_api;

import com.vk.dwzkf.alertor.commons.socket_api.get_users.GetUsersCallback;
import com.vk.dwzkf.alertor.commons.socket_api.users_state.UserConnectCallback;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SocketApiConfig<T, R> {
    public static final SocketApiConfig<AlertEvent, AlertCallback> ALERT_CONFIG;
    public static final SocketApiConfig<Void, GetUsersCallback> USERS_STATE;
    public static final SocketApiConfig<Void, UserConnectCallback> USER_CONNECT_STATE;

    static {
        ALERT_CONFIG = new SocketApiConfig<>(
                "alert",
                "alertCallback",
                AlertEvent.class,
                AlertCallback.class);

        USERS_STATE = new SocketApiConfig<>(
                "getUsers",
                "getUsersCallback",
                Void.class,
                GetUsersCallback.class
        );

        USER_CONNECT_STATE = new SocketApiConfig<>(null,
                "usersConnectStateCallback",
                Void.class,
                UserConnectCallback.class
        );
    }

    private final String eventEndpoint;
    private final String callbackEndpoint;
    private final Class<T> requestClass;
    private final Class<R> responseClass;
}
