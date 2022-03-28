package com.vk.dwzkf.alertor.alertor_client.ui.window;

import com.vk.dwzkf.alertor.alertor_client.listener.UserStateListener;
import com.vk.dwzkf.alertor.alertor_client_core.listener.SocketConnectorListener;
import com.vk.dwzkf.alertor.commons.socket_api.users_state.UserConnectData;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

@Component
@RequiredArgsConstructor
public class UsersUi extends JPanel implements SocketConnectorListener, UserStateListener {
    public static final Color CURRENT_USER_COLOR = new Color(89, 55, 222);
    private final DefaultListModel<Wrapper> dlm = new DefaultListModel<>();
    private JList<Wrapper> jList = new JList<>(dlm);

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class Wrapper {
        private JLabel label;
        @EqualsAndHashCode.Include
        private String uuid;
    }

    @PostConstruct
    public void configure() {
        final BoxLayout mgr = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(mgr);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        jList.setFixedCellHeight(25);
        jList.setFixedCellWidth(200);
        jList.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return ((Wrapper) value).label;
            }
        });
        add(new JLabel("Users"));
        add(wrapToScroll(jList));
    }

    private JScrollPane wrapToScroll(java.awt.Component c) {
        final JScrollPane scrollPane = new JScrollPane(c);
        scrollPane.setMaximumSize(scrollPane.getPreferredSize());
        return scrollPane;
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
    public void onConnected(UserConnectData connectedUser) {
        JLabel label = new JLabel(connectedUser.getUserData().getName());
        label.setBackground(new Color(connectedUser.getUserData().getColor()));
        label.setOpaque(true);
        if (connectedUser.isYou()) {
            label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            label.setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, label.getFont().getSize()));
        } else {
            label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, label.getFont().getSize()));
        }
        Wrapper wrapper = new Wrapper(label, connectedUser.getUserData().getUuid());
        dlm.addElement(wrapper);
    }

    @Override
    public void onDisconnected(UserConnectData disconnectedUser) {
        dlm.removeElement(new Wrapper(null, disconnectedUser.getUserData().getUuid()));
    }
}
