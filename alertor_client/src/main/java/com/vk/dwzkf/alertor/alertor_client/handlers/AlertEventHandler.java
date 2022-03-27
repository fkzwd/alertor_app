package com.vk.dwzkf.alertor.alertor_client.handlers;

import com.vk.dwzkf.alertor.alertor_client.alertor.AlertConfig;
import com.vk.dwzkf.alertor.alertor_client_core.handlers.EventHandler;
import com.vk.dwzkf.alertor.alertor_client.listener.AlertListener;
import com.vk.dwzkf.alertor.commons.socket_api.AlertCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlertEventHandler extends EventHandler<AlertCallback> {
    private final AlertListener alertListener;
    private final AlertConfig alertConfig;

    @Override
    public void handleEvent(AlertCallback event) {
        if (alertConfig.isAlertEnabled()) {
            alertListener.onAlert(event);
        }
    }
}
