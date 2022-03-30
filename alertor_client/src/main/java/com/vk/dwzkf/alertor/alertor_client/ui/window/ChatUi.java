package com.vk.dwzkf.alertor.alertor_client.ui.window;

import com.vk.dwzkf.alertor.alertor_client.alertor.AudioPlayer;
import com.vk.dwzkf.alertor.alertor_client.alertor.NoopCaret;
import com.vk.dwzkf.alertor.alertor_client.listener.MessageListener;
import com.vk.dwzkf.alertor.alertor_client_core.client.EventSender;
import com.vk.dwzkf.alertor.alertor_client_core.listener.SocketConnectorListener;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import com.vk.dwzkf.alertor.commons.socket_api.message.UserMessage;
import com.vk.dwzkf.alertor.commons.socket_api.message.UserMessageCallback;
import com.vk.dwzkf.alertor.commons.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.vk.dwzkf.alertor.alertor_client.utils.JavaSwingUtils.configureAutoscrollDown;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatUi extends JPanel implements MessageListener {
    private DefaultListModel<UserMessageCallback> dlm = new DefaultListModel<>();
    private JButton sendButton;
    private JButton clearChatButton;
    private JTextArea messageArea;
    private JList<UserMessageCallback> chatList;
    private final EventSender eventSender;
    @Qualifier("messagePlayer")
    private final AudioPlayer audioPlayer;
    //51.250.27.19

    @PostConstruct
    public void configure() {
        audioPlayer.init();
        setLayout(new BorderLayout());
        createElements();
        JScrollPane scrollPane = new JScrollPane(chatList);
        configureAutoscrollDown(scrollPane, chatList, dlm, 1);
        add(scrollPane);
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
        JScrollPane messageAreaScroll = new JScrollPane(messageArea);
        messagePanel.add(messageAreaScroll);
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons,BoxLayout.Y_AXIS));
        buttons.add(sendButton);
        buttons.add(clearChatButton);
        messagePanel.add(buttons);
        add(messagePanel, BorderLayout.SOUTH);
    }

    private void createElements() {
        sendButton = createSendButton();
        messageArea = createMessageArea();
        chatList = createChatList();
        clearChatButton = createClearChatButton();

        messageArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                sendButton.setEnabled(!messageArea.getText().isBlank());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                sendButton.setEnabled(!messageArea.getText().isBlank());

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                sendButton.setEnabled(!messageArea.getText().isBlank());
            }
        });

        sendButton.addActionListener(l -> {
            sendMessage();
        });


        InputMap inputMap = messageArea.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = messageArea.getActionMap();
        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        inputMap.put(enterKey, "enter");
        actionMap.put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea txtArea = (JTextArea) e.getSource();
                sendMessage();
                txtArea.setText("");
            }
        });

        KeyStroke shiftEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_DOWN_MASK, false);
        inputMap.put(shiftEnter, "shift-enter");
        actionMap.put("shift-enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea txtArea = (JTextArea) e.getSource();
                txtArea.setText(txtArea.getText()+"\n");
            }
        });


        sendButton.setMaximumSize(sendButton.getPreferredSize());
        messageArea.setMaximumSize(messageArea.getMaximumSize());
        chatList.setMaximumSize(chatList.getPreferredSize());
    }

    private JButton createClearChatButton() {
        JButton jButton = new JButton();
        jButton.setText("Clear");
        jButton.addActionListener(e -> {
            dlm.clear();
        });
        return jButton;
    }

    private void sendMessage() {
        if (!messageArea.getText().isBlank()) {
            eventSender.emit(SocketApiConfig.SEND_MESSAGE, new UserMessage(messageArea.getText()));
            messageArea.setText("");
        }
    }

    private JList<UserMessageCallback> createChatList() {
        JList<UserMessageCallback> chat = new JList<>(dlm);
        chat.setFixedCellWidth(120);
        chat.setCellRenderer(new ListCellRenderer<UserMessageCallback>() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<? extends UserMessageCallback> list, UserMessageCallback value, int index, boolean isSelected, boolean cellHasFocus) {
                JPanel jPanel = new JPanel();
                jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
                java.awt.Component messageComponent = createMessageComponent(value, isSelected, cellHasFocus, index);
                java.awt.Component userInfoComponent = createUserInfoComponent(value, isSelected, cellHasFocus, index);
                jPanel.add(userInfoComponent);
                jPanel.add(messageComponent);
                JPanel wrapper = new JPanel(new BorderLayout(5,0));
                setChatEntityColor(index, jPanel, userInfoComponent, value);

                wrapper.setBorder((
                                BorderFactory.createCompoundBorder(
                                        new EmptyBorder(3, 3, 0, 3),
                                        BorderFactory.createStrokeBorder(new BasicStroke(1.0f))
                                )
                        )
                );
                wrapper.add(createTimeComponent(value), BorderLayout.NORTH);
                wrapper.add(jPanel);
                return wrapper;
            }
        });
        chat.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return chat;
    }

    private java.awt.Component createTimeComponent(UserMessageCallback value) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochSecond(value.getTime()), ZoneId.systemDefault());
        final JLabel label = new JLabel(zdt.format(DateTimeUtils.DATE_TIME_FORMATTER));
        label.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        return label;
    }

    private java.awt.Component createUserInfoComponent(UserMessageCallback value, boolean isSelected, boolean cellHasFocus, int index) {
        JLabel userLabel = new JLabel(value.getUserData().getName() + ": ");
        userLabel.setOpaque(true);
        if (value.isYourMessage()) {
            userLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, userLabel.getFont().getSize()));
        } else {
            userLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, userLabel.getFont().getSize()));
        }
        userLabel.setAlignmentY(JLabel.TOP);
        userLabel.setVerticalTextPosition(JLabel.TOP);
        userLabel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        return userLabel;
    }

    private JTextArea createMessageComponent(UserMessageCallback value, boolean isSelected, boolean isCellHasFocus, int idx) {
        JTextArea jTextArea = new JTextArea(value.getMessage());
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setEditable(false);
        jTextArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return jTextArea;
    }

    private void setChatEntityColor(int idx, JPanel panel, java.awt.Component label, UserMessageCallback userMessage) {
        final Color color = new Color(userMessage.getUserData().getColor());
        panel.setBackground(color);
        label.setBackground(color);
    }

    private JTextArea createMessageArea() {
        final JTextArea jTextArea = new JTextArea();
        jTextArea.setLineWrap(true);
        jTextArea.setRows(3);
        return jTextArea;
    }

    private JButton createSendButton() {
        final JButton jButton = new JButton("Send");
        jButton.setEnabled(false);
        return jButton;
    }

    @Override
    public void onMessage(UserMessageCallback userMessage) {
        audioPlayer.play();
        dlm.addElement(userMessage);
    }
}
