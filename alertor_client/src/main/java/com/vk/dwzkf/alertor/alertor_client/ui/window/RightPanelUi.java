package com.vk.dwzkf.alertor.alertor_client.ui.window;

import com.vk.dwzkf.alertor.alertor_client.alertor.AlertConfig;
import com.vk.dwzkf.alertor.alertor_client_core.client.EventSender;
import com.vk.dwzkf.alertor.commons.socket_api.AlertEvent;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

@Component
@RequiredArgsConstructor
public class RightPanelUi extends JPanel {
    public static final String DISABLE_ALERT = "Disable alert";
    public static final String ENABLE_ALERT = "Enable alert";
    private JButton alertButton;
    private JButton alertConfigButton;
    private JLabel alertConfigStatus;
    private final AlertConfig alertConfig;
    private final EventSender eventSender;

    @PostConstruct
    public void configure() {
        setLayout(new GridLayout(3,1,10,10));
        alertButton = createAlertButton();
        alertConfigStatus = createAlertConfigStatus();
        alertConfigButton = createAlertConfigButton();

        add(alertButton);
        add(alertConfigButton);
        add(alertConfigStatus);
    }

    private JLabel createAlertConfigStatus() {
        JLabel configStatus = new JLabel(getStatusMessage(alertConfig.isAlertEnabled()));
        configStatus.setForeground(getStatusColor(alertConfig.isAlertEnabled()));
        return configStatus;
    }

    private String getStatusMessage(boolean alertEnabled) {
        return alertEnabled ? "Alert ENABLED!" : "Alert DISABLED!";
    }

    private Color getStatusColor(boolean alertEnabled) {
        return alertEnabled ? Color.GREEN : Color.RED;
    }

    private JButton createAlertConfigButton() {
        JButton jButton = new JButton(DISABLE_ALERT);
        jButton.addActionListener(e -> {
            if (jButton.getText().equalsIgnoreCase(DISABLE_ALERT)) {
                alertConfig.setAlertEnabled(false);
                alertConfigStatus.setText(getStatusMessage(false));
                alertConfigStatus.setForeground(getStatusColor(false));
                jButton.setText(ENABLE_ALERT);
            } else if (jButton.getText().equalsIgnoreCase(ENABLE_ALERT)) {
                alertConfig.setAlertEnabled(true);
                alertConfigStatus.setText(getStatusMessage(true));
                alertConfigStatus.setForeground(getStatusColor(true));
                jButton.setText(DISABLE_ALERT);
            }
        });
        return jButton;
    }

    private JButton createAlertButton() {
        JButton jButton = new JButton("GO SMOKE!");
        jButton.addActionListener(e -> {
            eventSender.emit(SocketApiConfig.ALERT_CONFIG, new AlertEvent("GO SMOKE!! ", 150, 30));
        });
        return jButton;
    }
}
