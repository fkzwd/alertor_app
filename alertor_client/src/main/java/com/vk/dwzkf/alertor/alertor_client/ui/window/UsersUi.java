package com.vk.dwzkf.alertor.alertor_client.ui.window;

import com.vk.dwzkf.alertor.alertor_client.listener.UserStateListener;
import com.vk.dwzkf.alertor.alertor_client_core.listener.SocketConnectorListener;
import com.vk.dwzkf.alertor.commons.entity.UserData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

@Component
@RequiredArgsConstructor
public class UsersUi extends JPanel implements SocketConnectorListener, UserStateListener {
    private final DefaultListModel<String> dlm = new DefaultListModel<>();
    private JList<String> jList = new JList<>(dlm);

    @PostConstruct
    public void configure() {
        final BoxLayout mgr = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(mgr);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        jList.setFixedCellHeight(25);
        jList.setFixedCellWidth(150);
        add(new JLabel("Users"));
        add(new JScrollPane(jList));
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

    @Override
    public void onConnected(UserData userData) {
        dlm.addElement(userData.getName());
    }

    @Override
    public void onDisconnected(UserData userData) {
        dlm.removeElement(userData.getName());
    }
}
