package com.vk.dwzkf.alertor.socket_server_core.command;

import com.vk.dwzkf.alertor.commons.entity.UserData;

public abstract class VoidSocketCommand<T> extends SocketCommand<T, Void> {
    @Override
    public final Void onEvent(UserData user, T arg) {
        handleEvent(user, arg);
        return null;
    }

    public abstract void handleEvent(UserData userData, T arg);
}
