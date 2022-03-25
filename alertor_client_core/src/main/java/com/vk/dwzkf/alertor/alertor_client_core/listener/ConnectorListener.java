package com.vk.dwzkf.alertor.alertor_client_core.listener;

public interface ConnectorListener {
    void onConnect(Object[] args);
    void onDisconnect(Object[] args);
    void onConnectError(Object[] args);
    void registerListener(SocketConnectorListener listener);
}
