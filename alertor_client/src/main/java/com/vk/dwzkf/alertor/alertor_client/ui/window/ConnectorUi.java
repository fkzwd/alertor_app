package com.vk.dwzkf.alertor.alertor_client.ui.window;

import com.vk.dwzkf.alertor.alertor_client_core.client.SocketClient;
import com.vk.dwzkf.alertor.alertor_client_core.listener.SocketConnectorListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@Component
@RequiredArgsConstructor
public class ConnectorUi extends JPanel implements SocketConnectorListener {
    private final SocketClient socketClient;
    private JLabel connectionState;
    private JTextField hostTextField;
    private JTextField portField;
    private JButton connectionButton;

    private JButton createConnectionButton() {
        JButton jButton = new JButton("Connect");
        jButton.addActionListener(event -> {
            if (jButton.getText().equalsIgnoreCase("connect")) {
                if (!portField.getText().matches("\\d{2,5}")
                        || !hostTextField.getText().matches("(localhost)|(\\d{1,3}(\\.\\d{1,3}){3})")) {
                    hostTextField.setText("Please enter valid HOST and PORT");
                } else {
                    socketClient.getSocketConfig().setHost(hostTextField.getText());
                    socketClient.getSocketConfig().setPort(Integer.parseInt(portField.getText()));
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
                connectionState.setForeground(Color.RED);
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
        return jTextField;
    }

    private JLabel createConnectionStateLabel() {
        JLabel label = new JLabel("Not connected");
        label.setForeground(Color.RED);
        return label;
    }

    @PostConstruct
    public void configure() {
        createElements();
        GridLayout mgr = new GridLayout();
        setLayout(mgr);
        mgr.setHgap(5);
        mgr.setVgap(5);
        setBorder(new EmptyBorder(15,15,15,15));
        add(hostTextField);
        add(portField);
        add(connectionButton);
        add(connectionState);
    }

    private void createElements() {
        hostTextField  = createHostTextField();
        connectionState = createConnectionStateLabel();
        connectionButton = createConnectionButton();
        portField = createPortField();
    }

    private JTextField createPortField() {
        JTextField jTextField = new JTextField(String.valueOf(socketClient.getSocketConfig().getPort()), 6);
        return jTextField;
    }

    @Override
    public void onConnect(Object[] args) {
        connectionState.setText("Connected");
        connectionState.setForeground(Color.GREEN);

        setCanEdit(false);
        connectionButton.setText("Disconnect");
        _repaint();
    }

    private void setCanEdit(boolean canConnect) {
        hostTextField.setEnabled(canConnect);
        portField.setEnabled(canConnect);
        _repaint();
    }

    @Override
    public void onDisconnect(Object[] args) {
        connectionState.setText("Not connected");
        connectionState.setForeground(Color.RED);
        setCanEdit(true);
        connectionButton.setText("Connect");

        _repaint();
    }

    @Override
    public void onConnectError(Object[] args) {
        connectionState.setText("Connect error");
        connectionState.setForeground(Color.RED);
        connectionButton.setText("Disconnect");

        _repaint();
    }

    private void onBadParam(String hostname) {

    }
}
