package com.vk.dwzkf.alertor.alertor_client.ui;

import com.vk.dwzkf.alertor.alertor_client_core.client.EventSender;
import com.vk.dwzkf.alertor.alertor_client_core.client.SocketClient;
import com.vk.dwzkf.alertor.commons.socket_api.AlertEvent;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import com.vk.dwzkf.alertor.ui_core.UiCommand;
import com.vk.dwzkf.alertor.ui_core.UiCore;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlertUi extends UiCore<Void> {
    private final EventSender eventSender;
    private String message = null;

    @Override
    public void configure() {
        addCommand(UiCommand.from(() -> String.format("Set message (%s)", message == null ? "" : message), () -> {
            System.out.print("Print message:");
            AlertUi.this.message = UiCore.nextString();
        }));

        addCommand(UiCommand.from(() -> String.format("Send (%s)", message == null ? "" : message), () -> {
            if (message != null) {
                eventSender.emit(SocketApiConfig.ALERT_CONFIG, new AlertEvent(message, 150, 30));
                AlertUi.this.end();
            } else {
                System.out.println("Message is null.");
            }
        }));
    }
}
