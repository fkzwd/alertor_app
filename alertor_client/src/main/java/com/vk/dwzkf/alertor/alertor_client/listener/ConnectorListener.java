package com.vk.dwzkf.alertor.alertor_client.listener;

public interface ConnectorListener {
    void onConnect(Object[] args);
    void onDisconnect(Object[] args);
    void onConnectError(Object[] args);
}
