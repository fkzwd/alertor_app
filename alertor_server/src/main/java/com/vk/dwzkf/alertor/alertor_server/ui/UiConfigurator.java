package com.vk.dwzkf.alertor.alertor_server.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UiConfigurator {
    private final List<ServerUiCommand> uiCommandList = new ArrayList<>();

    @Autowired(required = false)
    public void injectCommands(List<ServerUiCommand> commands) {
        uiCommandList.addAll(commands);
    }

    public AlertorServerUi configure(AlertorServerUi ui) {
        uiCommandList.forEach(ui::addCommand);
        return ui;
    }
}
