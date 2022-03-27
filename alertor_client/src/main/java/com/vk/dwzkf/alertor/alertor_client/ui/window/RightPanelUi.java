package com.vk.dwzkf.alertor.alertor_client.ui.window;

import com.vk.dwzkf.alertor.alertor_client.alertor.AlertConfig;
import com.vk.dwzkf.alertor.alertor_client.alertor.AudioAlertor;
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
    private JCheckBox alertEnabledCheckbox;
    private JCheckBox soundEnabledCheckbox;
    private final AlertConfig alertConfig;
    private final EventSender eventSender;

    @PostConstruct
    public void configure() {
        setLayout(new GridLayout(3,1,10,10));
        alertButton = createAlertButton();
        alertEnabledCheckbox = createAlertEnabledCheckbox();
        soundEnabledCheckbox = createSoundEnabledCheckbox();

        add(alertButton);
        add(alertEnabledCheckbox);
        add(soundEnabledCheckbox);
    }

    private JCheckBox createSoundEnabledCheckbox() {
        JCheckBox soundEnabled = new JCheckBox("Sound enabled");
        soundEnabled.setSelected(AudioAlertor.AudioConfig.enabled);
        soundEnabled.addActionListener(e -> {
            AudioAlertor.AudioConfig.enabled = soundEnabled.isSelected();
        });
        return soundEnabled;
    }

    private JCheckBox createAlertEnabledCheckbox() {
        JCheckBox alertEnabled = new JCheckBox("Alert enabled");
        alertEnabled.setSelected(alertConfig.isAlertEnabled());
        alertEnabled.addActionListener(e -> {
            alertConfig.setAlertEnabled(alertEnabled.isSelected());
        });
        return alertEnabled;
    }

    private JButton createAlertButton() {
        JButton jButton = new JButton("GO SMOKE!");
        jButton.addActionListener(e -> {
            eventSender.emit(SocketApiConfig.ALERT_CONFIG, new AlertEvent("GO SMOKE!! ", 150, 30));
        });
        return jButton;
    }
}
