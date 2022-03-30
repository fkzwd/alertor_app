package com.vk.dwzkf.alertor.alertor_client.ui.window;

import com.vk.dwzkf.alertor.alertor_client.alertor.AlertConfig;
import com.vk.dwzkf.alertor.alertor_client.alertor.AudioAlertor;
import com.vk.dwzkf.alertor.alertor_client.alertor.AudioPlayer;
import com.vk.dwzkf.alertor.alertor_client.alertor.JFrameAlertorConfig;
import com.vk.dwzkf.alertor.alertor_client.listener.AlertListener;
import com.vk.dwzkf.alertor.alertor_client_core.client.EventSender;
import com.vk.dwzkf.alertor.commons.socket_api.AlertCallback;
import com.vk.dwzkf.alertor.commons.socket_api.AlertEvent;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import com.vk.dwzkf.alertor.commons.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.vk.dwzkf.alertor.alertor_client.utils.JavaSwingUtils.configureAutoscrollDown;

@Component
@RequiredArgsConstructor
public class RightPanelUi extends JPanel implements AlertListener {
    private JButton alertButton;
    private JCheckBox alertEnabledCheckbox;
    private JCheckBox soundEnabledCheckbox;
    private JTextArea alertMessageArea;
    private JSlider audioSlider;
    private JSlider alphaSlider;
    private JButton sendAlertButton;
    private final AlertConfig alertConfig;
    private final EventSender eventSender;
    @Qualifier("messagePlayer")
    private final AudioPlayer audioPlayer;
    private final JFrameAlertorConfig screamerConfig;
    private final DefaultListModel<AlertCallback> dlm = new DefaultListModel<>();
    private JList<AlertCallback> alertArchive = new JList<>(dlm);
    private JButton alertArchiveClearButton;

    @PostConstruct
    public void configure() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        alertButton = createAlertButton();
        alertEnabledCheckbox = createAlertEnabledCheckbox();
        soundEnabledCheckbox = createSoundEnabledCheckbox();
        alertMessageArea = createAlertMessageArea();
        sendAlertButton = createSendAlertButton();
        audioSlider = createAudioSlider();
        alphaSlider = createAlphaSlider();
        configureAlertArchive();
        alertArchiveClearButton = createAlertClearButton();

        add(alertButton);
        add(alertEnabledCheckbox);
        add(soundEnabledCheckbox);
        add(new JLabel("Notification sound:"));
        add(audioSlider);
        add(new JLabel("Alert alpha:"));
        add(alphaSlider);
        add(new JLabel("Alert message:"));
        add(wrapToScroll(alertMessageArea));
        add(sendAlertButton);
        add(new JLabel("Alerts:"));
        add(configureAutoscrollDown(wrapToScroll(alertArchive), alertArchive, dlm, 2));
        add(alertArchiveClearButton);
    }

    private JButton createAlertClearButton() {
        JButton jButton = new JButton();
        jButton.setText("Clear archive");
        jButton.addActionListener(e -> {
            dlm.clear();
        });
        return jButton;
    }

    private void configureAlertArchive() {
        alertArchive.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                AlertCallback alert = (AlertCallback) value;
                JPanel jPanel = new JPanel();
                jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
                JLabel usernameLabel = new JLabel("[" + alert.getUserData().getName().substring(0, 6) + "...]");
                usernameLabel.setBackground(new Color(alert.getUserData().getColor()));
                usernameLabel.setOpaque(true);
                jPanel.add(usernameLabel);
                ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochSecond(alert.getTime()), ZoneId.systemDefault());
                jPanel.add(new JLabel(" alerted at: "+zdt.format(DateTimeUtils.DATE_TIME_FORMATTER)));
                jPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
                return jPanel;
            }
        });
    }

    @Override
    public void onAlert(AlertCallback alertCallback) {
        dlm.addElement(alertCallback);
    }

    private JSlider createAlphaSlider() {
        JSlider jSlider = new JSlider();
        jSlider.setMaximum(255);
        jSlider.setMinimum(0);
        jSlider.setValue(screamerConfig.getAlpha());
        jSlider.setToolTipText("Alert opacity");
        jSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                screamerConfig.setAlpha(jSlider.getValue());
            }
        });
        return jSlider;
    }

    private JSlider createAudioSlider() {
        JSlider jSlider = new JSlider();
        jSlider.setMaximum(100);
        jSlider.setMinimum(0);
        jSlider.setValue(jSlider.getMaximum());
        jSlider.setToolTipText("Message volume");
        jSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                audioPlayer.getAudioConfig().setVolume(jSlider.getValue() / 100.0f);
            }
        });
        return jSlider;
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
        JCheckBox soundEnabled = new JCheckBox("Alert Sound enabled");
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
