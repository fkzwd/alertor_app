package com.vk.dwzkf.alertor.alertor_server.ui;

import com.vk.dwzkf.alertor.socket_server_core.config.IOSocketServer;
import com.vk.dwzkf.alertor.ui_core.UiCore;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlertorServerUi extends UiCore<Void> {
    private final IOSocketServer socketServer;

    @Override
    public void configure() {

    }

    @Override
    public void onTerminate() {
        socketServer.stop();
    }
}
