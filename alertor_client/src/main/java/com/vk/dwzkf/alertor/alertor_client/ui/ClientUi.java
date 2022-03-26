package com.vk.dwzkf.alertor.alertor_client.ui;

import com.vk.dwzkf.alertor.alertor_client_core.client.EventSender;
import com.vk.dwzkf.alertor.alertor_client_core.client.SocketClient;
import com.vk.dwzkf.alertor.commons.socket_api.AlertEvent;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import com.vk.dwzkf.alertor.ui_core.UiCommand;
import com.vk.dwzkf.alertor.ui_core.UiCore;
import lombok.RequiredArgsConstructor;

import static com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig.ALERT_CONFIG;

@RequiredArgsConstructor
public class ClientUi extends UiCore<Void> {
    private final EventSender eventSender;
    private final SocketClient socketClient;

    @Override
    public void configure() {
        addCommand(UiCommand.from("Send alert", () -> new AlertUi(eventSender).start()));
        addCommand(UiCommand.from("Restart", socketClient::restart));
        addCommand(UiCommand.from("GO SMOKE!", () -> eventSender.emit(ALERT_CONFIG, new AlertEvent("GO KURIT! ", 150, 35))));
    }

    @Override
    public void onTerminate() {
        socketClient.stop();
        System.exit(0);
    }
}
