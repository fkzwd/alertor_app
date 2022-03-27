package com.vk.dwzkf.alertor.alertor_client.ui.window;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

@Component
public class ChatUi extends JPanel {
    @PostConstruct
    public void configure() {
        JLabel jLabel = new JLabel("Chat would be here...");
        jLabel.setAlignmentX(0);
        jLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
        jLabel.setForeground(new Color(227,55,16));
        jLabel.setAlignmentY(0);
        add(jLabel);
    }
}
