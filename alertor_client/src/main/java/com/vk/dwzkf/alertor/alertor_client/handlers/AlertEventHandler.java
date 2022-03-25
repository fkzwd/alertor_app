package com.vk.dwzkf.alertor.alertor_client.handlers;

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

    @Override
    public void handleEvent(AlertCallback event) {
        alertListener.onAlert(event);
    }
}
