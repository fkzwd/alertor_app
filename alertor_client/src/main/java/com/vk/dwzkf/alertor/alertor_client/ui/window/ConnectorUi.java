package com.vk.dwzkf.alertor.alertor_client.ui.window;

import com.vk.dwzkf.alertor.alertor_client_core.client.SocketClient;
import com.vk.dwzkf.alertor.alertor_client_core.listener.SocketConnectorListener;
import com.vk.dwzkf.alertor.commons.configurators.Constants;
import com.vk.dwzkf.alertor.commons.configurators.PropertyListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.Map;

import static com.vk.dwzkf.alertor.alertor_client.ui.window.RightPanelUi.USER_COLOR;

@Component
@RequiredArgsConstructor
public class ConnectorUi extends JPanel implements SocketConnectorListener, PropertyListener {
    public static final Color BAD_COLOR = new Color(171, 0, 40);
    public static final Color GOOD_COLOR = new Color(74, 171, 0);
    public static final String PORT_REGEX = "\\d{2,5}";
    public static final String HOST_REGEX = "(localhost)|(\\d{1,3}(\\.\\d{1,3}){3})";
    private final SocketClient socketClient;
    private JLabel connectionState;
    private JTextField hostTextField;
    private JTextField portField;
    private JTextField nameField;
    private JButton connectionButton;

    public void updateConfig() {
        hostTextField.setText(socketClient.getSocketConfig().getHost());
        portField.setText(String.valueOf(socketClient.getSocketConfig().getPort()));
        nameField.setText(socketClient.getSocketConfig().getName());
    }

    @Override
    public List<String> properties() {
        return List.of(USER_COLOR);
    }

    @Override
    public void onLoaded(Map<String, String> props) {
        socketClient.getSocketConfig().setColor(props.get(USER_COLOR));
    }

    @Override
    public void onUpdated(String property, String value) {
        socketClient.getSocketConfig().setColor(value);
    }

    private JButton createConnectionButton() {
        JButton jButton = new JButton("Connect");
        jButton.addActionListener(event -> {
            if (jButton.getText().equalsIgnoreCase("connect")) {
                if (portField.getText().matches(PORT_REGEX)
                        && hostTextField.getText().matches(HOST_REGEX) && nameField.getText().matches(Constants.SOCKET_NAME_REGEX)) {
                            hostTextField.setForeground(null);
                            socketClient.getSocketConfig().setHost(hostTextField.getText());
                            socketClient.getSocketConfig().setPort(Integer.parseInt(portField.getText()));
                            socketClient.getSocketConfig().setName(nameField.getText());
                            socketClient.disconnect();
                            socketClient.initSocket();
                            socketClient.connect();
                            setCanEdit(false);
                            jButton.setText("Disconnect");
                        }
            } else if (jButton.getText().equalsIgnoreCase("disconnect")) {
                socketClient.disconnect();
                jButton.setText("Connect");
                connectionState.setText("Not connected");
                setConnectedColor(false);
                setCanEdit(true);
            }
            _repaint();
        });
        return jButton;
    }

    private void _repaint() {
        ConnectorUi.this.repaint();
    }

    private JTextField createHostTextField() {
        JTextField jTextField = new JTextField(socketClient.getSocketConfig().getHost(), 25);
        jTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleHostChanged(jTextField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleHostChanged(jTextField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleHostChanged(jTextField);
            }
        });
        return jTextField;
    }

    private JLabel createConnectionStateLabel() {
        return new JLabel("Not connected");
    }

    @PostConstruct
    public void configure() {
        createElements();
        setBackground(Color.LIGHT_GRAY);
        GridLayout mgr = new GridLayout();
        setLayout(mgr);
        mgr.setHgap(5);
        mgr.setVgap(5);
        setBorder(new EmptyBorder(15,15,15,15));
        add(getHintLabel("Host:"));
        add(hostTextField);
        add(getHintLabel("Port:"));
        add(portField);
        add(getHintLabel("Name:"));
        add(nameField);
        add(connectionButton);
        add(connectionState);
    }

    private JLabel getHintLabel(String s) {
        final JLabel label = new JLabel(s);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private void createElements() {
        hostTextField  = createHostTextField();
        nameField = createNameField();
        connectionState = createConnectionStateLabel();
        setConnectedColor(false);
        connectionButton = createConnectionButton();
        portField = createPortField();
    }

    private JTextField createNameField() {
        JTextField jTextField = new JTextField(socketClient.getSocketConfig().getName());
        jTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleNameFieldChanged(jTextField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleNameFieldChanged(jTextField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleNameFieldChanged(jTextField);
            }
        });
        return jTextField;
    }

    private void handleNameFieldChanged(JTextField jTextField) {
        if (jTextField.getText().matches(Constants.SOCKET_NAME_REGEX)) {
            jTextField.setForeground(null);
            connectionButton.setEnabled(true);
        } else {
            jTextField.setForeground(BAD_COLOR);
            connectionButton.setEnabled(false);
        }
    }

    private void handlePortChanged(JTextField portField) {
        if (portField.getText().matches(PORT_REGEX)) {
            portField.setForeground(null);
            connectionButton.setEnabled(true);
        } else {
            portField.setForeground(BAD_COLOR);
            connectionButton.setEnabled(false);
        }
    }

    private void handleHostChanged(JTextField hostField) {
        if (hostField.getText().matches(HOST_REGEX)) {
            hostField.setForeground(null);
            connectionButton.setEnabled(true);
        } else {
            hostField.setForeground(BAD_COLOR);
            connectionButton.setEnabled(false);
        }
    }

    private JTextField createPortField() {
        JTextField jTextField = new JTextField(String.valueOf(socketClient.getSocketConfig().getPort()), 6);
        jTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handlePortChanged(jTextField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handlePortChanged(jTextField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handlePortChanged(jTextField);
            }
        });
        return jTextField;
    }

    @Override
    public void onConnect(Object[] args) {
        connectionState.setText("Connected");
        setConnectedColor(true);

        setCanEdit(false);
        connectionButton.setText("Disconnect");
        _repaint();
    }

    private void setCanEdit(boolean canConnect) {
        hostTextField.setEnabled(canConnect);
        portField.setEnabled(canConnect);
        nameField.setEnabled(canConnect);
        _repaint();
    }

    @Override
    public void onDisconnect(Object[] args) {
        connectionState.setText("Not connected");
        setConnectedColor(false);
        setCanEdit(true);
        connectionButton.setText("Connect");

        _repaint();
    }

    private void setConnectedColor(boolean connected) {
        if (connected) {
            connectionState.setForeground(GOOD_COLOR);
        } else {
            connectionState.setForeground(BAD_COLOR);
        }
    }

    @Override
    public void onConnectError(Object[] args) {
        connectionState.setText("Connect error");
        setConnectedColor(false);
        connectionButton.setText("Disconnect");

        _repaint();
    }
}
