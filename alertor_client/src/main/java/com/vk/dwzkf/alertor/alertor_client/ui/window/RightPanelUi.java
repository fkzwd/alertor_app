package com.vk.dwzkf.alertor.alertor_client.ui.window;

import com.vk.dwzkf.alertor.alertor_client.alertor.AlertConfig;
import com.vk.dwzkf.alertor.alertor_client.alertor.AudioConfig;
import com.vk.dwzkf.alertor.alertor_client.alertor.AudioPlayer;
import com.vk.dwzkf.alertor.alertor_client.alertor.JFrameAlertorConfig;
import com.vk.dwzkf.alertor.alertor_client.config.PropertiesConfigurator;
import com.vk.dwzkf.alertor.alertor_client.config.PropertyListener;
import com.vk.dwzkf.alertor.alertor_client.listener.AlertListener;
import com.vk.dwzkf.alertor.alertor_client_core.client.EventSender;
import com.vk.dwzkf.alertor.commons.socket_api.AlertCallback;
import com.vk.dwzkf.alertor.commons.socket_api.AlertEvent;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import com.vk.dwzkf.alertor.commons.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.vk.dwzkf.alertor.alertor_client.utils.JavaSwingUtils.configureAutoscrollDown;

@Component
@RequiredArgsConstructor
@Slf4j
public class RightPanelUi extends JPanel implements AlertListener, PropertyListener {
    public static final String AUDIO_RESOURCE = "audio_resource";
    public static final String AUDIO_FILE = "audio_file";
    public static final String ALPHA_PROP = "alpha_prop";
    public static final String NOTIFICATION_PROP = "notification_prop";
    public static final String USER_COLOR = "user_color";
    public static final int PREVIEW_LENGTH = 80;

    private JButton alertButton;
    private JCheckBox alertEnabledCheckbox;
    private JCheckBox soundEnabledCheckbox;
    private JTextArea alertMessageArea;
    private JSlider audioSlider;
    private JSlider alphaSlider;
    private JButton sendAlertButton;
    private JComboBox<String> alertorDefaultAudio;
    private final AlertConfig alertConfig;
    private final EventSender eventSender;
    @Qualifier("messagePlayer")
    private final AudioPlayer messagePlayer;
    @Qualifier("alertPlayer")
    private final AudioPlayer alertPlayer;
    private final JFrameAlertorConfig screamerConfig;
    private final DefaultListModel<AlertCallback> dlm = new DefaultListModel<>();
    private JList<AlertCallback> alertArchive = new JList<>(dlm);
    private JButton alertArchiveClearButton;
    private final PropertiesConfigurator propertiesConfigurator;
    private JSlider redSlider;
    private JSlider greenSlider;
    private JSlider blueSlider;
    private JPanel previewColor = new JPanel();
    private JLabel previewLabel = new JLabel("Your color");

    @PostConstruct
    public void configure() {
        previewColor.setLayout(new BoxLayout(previewColor, BoxLayout.Y_AXIS));
        previewColor.setSize(PREVIEW_LENGTH, PREVIEW_LENGTH);
        previewColor.setMaximumSize(new Dimension(PREVIEW_LENGTH, PREVIEW_LENGTH));
        previewColor.setMinimumSize(new Dimension(PREVIEW_LENGTH, PREVIEW_LENGTH));
        previewColor.setPreferredSize(new Dimension(PREVIEW_LENGTH, PREVIEW_LENGTH));
        previewColor.setOpaque(true);
        previewColor.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        previewLabel.setAlignmentY(0f);
        previewLabel.setAlignmentX(0f);
        JPanel previewBlock = new JPanel();
        previewBlock.setLayout(new BoxLayout(previewBlock, BoxLayout.Y_AXIS));
        previewBlock.add(previewLabel);
        previewBlock.add(previewColor);

        final BoxLayout mgr = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(mgr);
        setAlignmentX(1.0f);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        alertButton = createAlertButton();
        alertEnabledCheckbox = createAlertEnabledCheckbox();
        soundEnabledCheckbox = createSoundEnabledCheckbox();
        alertMessageArea = createAlertMessageArea();
        sendAlertButton = createSendAlertButton();
        audioSlider = createAudioSlider();
        alphaSlider = createAlphaSlider();
        configureAlertArchive();
        alertArchiveClearButton = createAlertClearButton();
        alertorDefaultAudio = createAlertorAudioComboBox();
        redSlider = createRedSlider();
        greenSlider = createGreenSlider();
        blueSlider = createBlueSlider();

        add(alertButton);
        add(alertEnabledCheckbox);
        add(soundEnabledCheckbox);
        add(new JLabel("Notification sound:"));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
        jPanel.add(audioSlider);
        jPanel.setAlignmentX(LEFT_ALIGNMENT);
        add(jPanel);
        add(new JLabel("Alert alpha:"));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.X_AXIS));
        jPanel2.add(alphaSlider);
        jPanel2.setAlignmentX(LEFT_ALIGNMENT);
        add(jPanel2);
        add(new JLabel("Alert message:"));
        add(wrapToScroll(alertMessageArea));
        add(sendAlertButton);
        add(new JLabel("Alerts:"));
        add(configureAutoscrollDown(wrapToScroll(alertArchive), alertArchive, dlm, 2));
        add(alertArchiveClearButton);
        add(alertorDefaultAudio);
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.X_AXIS));
        colorPanel.setAlignmentX(LEFT_ALIGNMENT);
        colorPanel.add(redSlider);
        colorPanel.add(greenSlider);
        colorPanel.add(blueSlider);
        colorPanel.add(previewBlock);
        add(colorPanel);
    }

    private JSlider createBlueSlider() {
        JSlider jSlider = createSlider(255);
        return jSlider;
    }

    private JSlider createGreenSlider() {
        JSlider jSlider = createSlider(255);
        return jSlider;
    }

    private JSlider createRedSlider() {
        JSlider jSlider = createSlider(255);
        return jSlider;
    }

    private JSlider createSlider(int max) {
        JSlider jSlider = new JSlider();
        jSlider.setAlignmentX(LEFT_ALIGNMENT);
        jSlider.setMinimum(0);
        jSlider.setMaximum(max);
        jSlider.setOrientation(JSlider.VERTICAL);
        jSlider.addChangeListener(l -> {
            previewColor.setBackground(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
            propertiesConfigurator.update(USER_COLOR,
                    //0x000000 << 0xFF
                    String.valueOf(
                            redSlider.getValue() << 16 | greenSlider.getValue() << 8 | blueSlider.getValue()
                    )
            );
        });
        return jSlider;
    }

    private JComboBox<String> createAlertorAudioComboBox() {
        JComboBox<String> select = new JComboBox<>(AudioConfig.DEFAULT_AUDIOS.toArray(new String[0]));
        select.addActionListener(l -> {
            propertiesConfigurator.update(AUDIO_RESOURCE, Objects.requireNonNull(select.getSelectedItem()).toString());
        });
        select.setMaximumSize(select.getPreferredSize());
        select.setAlignmentX(LEFT_ALIGNMENT);
        return select;
    }

    private JButton createAlertClearButton() {
        JButton jButton = new JButton();
        jButton.setText("Clear archive");
        jButton.addActionListener(e -> {
            dlm.clear();
        });
        jButton.setAlignmentX(LEFT_ALIGNMENT);
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
                jPanel.add(new JLabel(" alerted at: " + zdt.format(DateTimeUtils.DATE_TIME_FORMATTER)));
                jPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
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
                propertiesConfigurator.update(ALPHA_PROP, String.valueOf(jSlider.getValue()));
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
                messagePlayer.getAudioConfig().setVolume(jSlider.getValue() / 100.0f);
                propertiesConfigurator.update(NOTIFICATION_PROP, String.valueOf(jSlider.getValue()));
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
        soundEnabled.setSelected(alertPlayer.getAudioConfig().isEnabled());
        soundEnabled.addActionListener(e -> {
            alertPlayer.getAudioConfig().setEnabled(soundEnabled.isSelected());
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
            eventSender.emit(SocketApiConfig.ALERT_CONFIG, new AlertEvent("GO SMOKE!! ", 150, 40));
        });
        jButton.setAlignmentX(0);
        jButton.setAlignmentY(0);
        return jButton;
    }

    @Override
    public List<String> properties() {
        return List.of(AUDIO_FILE, AUDIO_RESOURCE, ALPHA_PROP, NOTIFICATION_PROP, USER_COLOR);
    }

    @Override
    public void onLoaded(Map<String, String> props) {
        if (props.get("audio_resource") != null) {
            alertPlayer.reloadAudioResource(props.get("audio_resource"));
            alertorDefaultAudio.setSelectedItem(props.get("audio_resource"));
        } else if (props.get("audio_file") != null) {
            alertPlayer.reloadAudioFile(props.get("audio_file"));
        }
        if (props.get(ALPHA_PROP) != null) {
            onUpdated(ALPHA_PROP, props.get(ALPHA_PROP));
        }
        if (props.get(NOTIFICATION_PROP) != null) {
            onUpdated(NOTIFICATION_PROP, props.get(NOTIFICATION_PROP));
        }
        if (props.get(USER_COLOR) != null) {
            onUpdated(USER_COLOR, props.get(USER_COLOR));
        }
    }

    @Override
    public void onUpdated(String property, String value) {
        if (property.equalsIgnoreCase(AUDIO_FILE)) {
            alertPlayer.reloadAudioFile(value);
        } else if (property.equalsIgnoreCase(AUDIO_RESOURCE)) {
            alertPlayer.reloadAudioResource(value);
        } else if (property.equalsIgnoreCase(NOTIFICATION_PROP)) {
            try {
                int val = Integer.parseInt(value);
                if (val < 0 || val > 100) {
                    propertiesConfigurator.update(NOTIFICATION_PROP, "50");
                    return;
                }
                messagePlayer.getAudioConfig().setVolume(val / 100.0f);
                audioSlider.setValue(val);
            } catch (NumberFormatException e) {
                log.error("Bad property loaded", e);
                messagePlayer.getAudioConfig().setVolume(1.0f);
                propertiesConfigurator.update(NOTIFICATION_PROP, "1");
            }
        } else if (property.equalsIgnoreCase(ALPHA_PROP)) {
            try {
                int val = Integer.parseInt(value);
                if (val < 0 || val > 255) {
                    propertiesConfigurator.update(ALPHA_PROP, "90");
                    return;
                }
                alphaSlider.setValue(val);
                screamerConfig.setAlpha(val);
            } catch (NumberFormatException e) {
                log.error("Bad property loaded.", e);
                propertiesConfigurator.update(ALPHA_PROP, "90");
            }
        } else if (property.equalsIgnoreCase(USER_COLOR)) {
            try {
                int color = Integer.parseInt(value);
                int r = 0x000000FF & (color >> 16);
                int g = 0x000000FF & (color >> 8);
                int b = 0x000000FF & color;
                redSlider.setValue(r);
                greenSlider.setValue(g);
                blueSlider.setValue(b);
                previewColor.setBackground(new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
            } catch (Exception e) {
                log.error("Error on color update.", e);
                propertiesConfigurator.update(USER_COLOR, String.valueOf(0xa311d4));
            }
        }
    }
}
