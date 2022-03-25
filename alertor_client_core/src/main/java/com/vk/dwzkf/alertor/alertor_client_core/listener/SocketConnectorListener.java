package com.vk.dwzkf.alertor.alertor_client_core.listener;

public interface SocketConnectorListener {
    void onConnect(Object[] args);
    void onDisconnect(Object[] args);
    void onConnectError(Object[] args);
}
