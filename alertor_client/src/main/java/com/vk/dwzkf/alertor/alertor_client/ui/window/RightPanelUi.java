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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

@Component
@RequiredArgsConstructor
public class RightPanelUi extends JPanel {
    private JButton alertButton;
    private JCheckBox alertEnabledCheckbox;
    private JCheckBox soundEnabledCheckbox;
    private JTextArea alertMessageArea;
    private JButton sendAlertButton;
    private final AlertConfig alertConfig;
    private final EventSender eventSender;

    @PostConstruct
    public void configure() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        alertButton = createAlertButton();
        alertEnabledCheckbox = createAlertEnabledCheckbox();
        soundEnabledCheckbox = createSoundEnabledCheckbox();
        alertMessageArea = createAlertMessageArea();
        sendAlertButton = createSendAlertButton();


        add(alertButton);
        add(alertEnabledCheckbox);
        add(soundEnabledCheckbox);
        add(new JLabel("Alert message:"));
        add(wrapToScroll(alertMessageArea));
        add(sendAlertButton);
    }

    private JButton createSendAlertButton() {
        JButton jButton = new JButton("Send alert");
        jButton.setEnabled(false);
        jButton.setAlignmentX(0);
        jButton.setAlignmentY(0);
        jButton.addActionListener(l -> {
            eventSender.emit(SocketApiConfig.ALERT_CONFIG, new AlertEvent(alertMessageArea.getText(), 150, 30));
        });
        return jButton;
    }

    private JScrollPane wrapToScroll(java.awt.Component c) {
        JScrollPane areaScroll = new JScrollPane(c);
        areaScroll.setMaximumSize(areaScroll.getPreferredSize());
        areaScroll.setAlignmentX(0);
        areaScroll.setAlignmentY(0);
        return areaScroll;
    }

    private JTextArea createAlertMessageArea() {
        JTextArea jTextArea = new JTextArea();
        jTextArea.setLineWrap(true);
        jTextArea.setRows(4);
        jTextArea.setColumns(25);
        jTextArea.setMaximumSize(jTextArea.getPreferredSize());
        jTextArea.setAlignmentX(0);
        jTextArea.setAlignmentY(0);
        jTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                sendAlertButton.setEnabled(!jTextArea.getText().isBlank());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                sendAlertButton.setEnabled(!jTextArea.getText().isBlank());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                sendAlertButton.setEnabled(!jTextArea.getText().isBlank());
            }
        });
        return jTextArea;
    }

    private JCheckBox createSoundEnabledCheckbox() {
        JCheckBox soundEnabled = new JCheckBox("Sound enabled");
        soundEnabled.setSelected(AudioAlertor.AudioConfig.enabled);
        soundEnabled.addActionListener(e -> {
            AudioAlertor.AudioConfig.enabled = soundEnabled.isSelected();
        });
        soundEnabled.setAlignmentX(0);
        soundEnabled.setAlignmentY(0);
        soundEnabled.setForeground(Color.DARK_GRAY);
        return soundEnabled;
    }

    private JCheckBox createAlertEnabledCheckbox() {
        JCheckBox alertEnabled = new JCheckBox("Alert enabled");
        alertEnabled.setSelected(alertConfig.isAlertEnabled());
        alertEnabled.addActionListener(e -> {
            alertConfig.setAlertEnabled(alertEnabled.isSelected());
        });
        alertEnabled.setAlignmentX(0);
        alertEnabled.setAlignmentY(0);
        alertEnabled.setForeground(Color.DARK_GRAY);
        return alertEnabled;
    }

    private JButton createAlertButton() {
        JButton jButton = new JButton("GO SMOKE!");
        jButton.addActionListener(e -> {
            eventSender.emit(SocketApiConfig.ALERT_CONFIG, new AlertEvent("GO SMOKE!! ", 150, 30));
        });
        jButton.setAlignmentX(0);
        jButton.setAlignmentY(0);
        return jButton;
    }
}
