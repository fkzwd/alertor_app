package com.vk.dwzkf.alertor.alertor_client.ui.window;

import com.vk.dwzkf.alertor.alertor_client.alertor.AudioPlayer;
import com.vk.dwzkf.alertor.alertor_client.listener.MessageListener;
import com.vk.dwzkf.alertor.alertor_client_core.client.EventSender;
import com.vk.dwzkf.alertor.alertor_client_core.listener.SocketConnectorListener;
import com.vk.dwzkf.alertor.commons.socket_api.SocketApiConfig;
import com.vk.dwzkf.alertor.commons.socket_api.message.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatUi extends JPanel implements MessageListener, SocketConnectorListener {
    private DefaultListModel<UserMessage> dlm = new DefaultListModel<>();
    private JButton sendButton;
    private JTextArea messageArea;
    private JList<UserMessage> chatList;
    private final EventSender eventSender;
    private final AudioPlayer audioPlayer = new AudioPlayer("492739-wood-block-droplet-18.wav");

    @PostConstruct
    public void configure() {
        audioPlayer.init();
        setLayout(new BorderLayout());
        createElements();
        JScrollPane scrollPane = new JScrollPane(chatList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            private int prevMaxSize = -1;
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (prevMaxSize == -1) {
                    prevMaxSize = e.getAdjustable().getMaximum();
                }
                if (prevMaxSize != e.getAdjustable().getMaximum()) {
                    if (chatList.getLastVisibleIndex() != -1) {
                        if (chatList.getLastVisibleIndex() == dlm.size() - 1) {
                            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
                        }
                    }
                }
                prevMaxSize = e.getAdjustable().getMaximum();
            }
        });
        add(scrollPane);
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
        JScrollPane messageAreaScroll = new JScrollPane(messageArea);
        messagePanel.add(messageAreaScroll);
        messagePanel.add(sendButton);
        add(messagePanel, BorderLayout.SOUTH);
    }

    private void createElements() {
        sendButton = createSendButton();
        messageArea = createMessageArea();
        chatList = createChatList();

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

    private void sendMessage() {
        if (!messageArea.getText().isBlank()) {
            eventSender.emit(SocketApiConfig.SEND_MESSAGE, new UserMessage(messageArea.getText(), null));
            messageArea.setText("");
        }
    }

    private JList<UserMessage> createChatList() {
        JList<UserMessage> chat = new JList<>(dlm);
        chat.setFixedCellWidth(120);
        chat.setCellRenderer(new ListCellRenderer<UserMessage>() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<? extends UserMessage> list, UserMessage value, int index, boolean isSelected, boolean cellHasFocus) {
                JPanel jPanel = new JPanel();
                jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
                JTextArea jTextArea = new JTextArea(value.getMessage());
                jTextArea.setLineWrap(true);
                jTextArea.setWrapStyleWord(true);
                jTextArea.setEditable(false);
                jTextArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                JLabel userLabel = new JLabel(value.getUserData().getName() + ": ");
                userLabel.setOpaque(true);
                setChatEntityColor(index, jPanel, userLabel);
                userLabel.setAlignmentY(JLabel.TOP);
                userLabel.setVerticalTextPosition(JLabel.TOP);
                userLabel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
                jPanel.add(userLabel);
                jPanel.add(jTextArea);
                JPanel wrapper = new JPanel(new BorderLayout());

                wrapper.setBorder((
                                BorderFactory.createCompoundBorder(
                                        new EmptyBorder(3, 3, 0, 3),
                                        BorderFactory.createStrokeBorder(new BasicStroke(1.0f))
                                )
                        )
                );
                wrapper.add(jPanel);
                return wrapper;
            }
        });
        chat.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return chat;
    }

    private void setChatEntityColor(int idx, JPanel panel, JLabel label) {
        Color c;
        if (idx % 2 == 0) {
            c = new Color(204,232,252);
        } else {
            c = new Color(143,208,255);
        }
        panel.setBackground(c);
        label.setBackground(c);
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
    public void onMessage(UserMessage userMessage) {
        audioPlayer.play();
        dlm.addElement(userMessage);
    }

    @Override
    public void onConnect(Object[] args) {
    }

    @Override
    public void onDisconnect(Object[] args) {
        dlm.clear();
    }

    @Override
    public void onConnectError(Object[] args) {
        dlm.clear();
    }
}
