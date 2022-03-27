package com.vk.dwzkf.alertor.alertor_client.ui.window;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
@RequiredArgsConstructor
public class MainUi extends JFrame {
    private final ConnectorUi connectorUi;
    private final RightPanelUi rightPanelUi;

    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    public static final double COEFFICIENT = 0.8;
    private static final int width = (int) (toolkit.getScreenSize().width * COEFFICIENT);
    private static final int height = (int) (toolkit.getScreenSize().height * COEFFICIENT);
    private static final int offsetHeight = (toolkit.getScreenSize().height - height)/2;
    private static final int offsetWidth = (toolkit.getScreenSize().width - width)/2;

    {
        setLayout(new BorderLayout());
        setBounds(offsetWidth,offsetHeight, width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    public void start() {
        add(connectorUi, BorderLayout.NORTH);
        add(rightPanelUi, BorderLayout.EAST);
        setVisible(true);
    }
}
