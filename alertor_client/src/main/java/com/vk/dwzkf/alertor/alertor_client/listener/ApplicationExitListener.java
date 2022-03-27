package com.vk.dwzkf.alertor.alertor_client.listener;

import com.vk.dwzkf.alertor.alertor_client.repository.ConfigRepository;
import com.vk.dwzkf.alertor.alertor_client.ui.window.ConnectorUi;
import com.vk.dwzkf.alertor.alertor_client_core.client.SocketClient;
import com.vk.dwzkf.alertor.alertor_client_core.config.SocketConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationExitListener {
    private final ConfigRepository configRepository;
    private final SocketClient socketClient;
    private final ConnectorUi connectorUi;

    @EventListener(ApplicationStartedEvent.class)
    public void onStart() {
        SocketConfig config = configRepository.getConfig();
        if (config != null) {
            socketClient.getSocketConfig().setName(config.getName());
            socketClient.getSocketConfig().setPort(config.getPort());
            socketClient.getSocketConfig().setHost(config.getHost());
            socketClient.getSocketConfig().setContext(config.getContext());
            socketClient.getSocketConfig().setProtocol(config.getProtocol());
            connectorUi.updateConfig();
        }
    }

    @EventListener(ContextClosedEvent.class)
    public void onExit() {
        configRepository.saveConfig(socketClient.getSocketConfig());
    }
}
