package com.vk.dwzkf.alertor.socket_server_core.command;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import com.vk.dwzkf.alertor.socket_server_core.response.ErrorResponse;

public abstract class SocketCommand<T, R> {
    public abstract R onEvent(UserData user, T arg);
    public ErrorResponse handleError(UserData userData, String endpoint, T arg, Throwable t) {
        return new ErrorResponse(String.format("Cannot handle event %s from %s", endpoint, userData.getName()), t.getMessage());
    }
}
