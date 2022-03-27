package com.vk.dwzkf.alertor.alertor_client.alertor;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@Slf4j
public class JFrameAlertor extends JFrame {
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private final java.util.List<java.awt.Component> lables = new ArrayList<>();
    private final int alertCycles;
    private final int alertTimeoutMs;
    private static final Font font = new Font(Font.SERIF, Font.BOLD, 35);
    private final String message;

    public JFrameAlertor(int alertCycles, int alertTimeoutMs, String message) throws HeadlessException {
        this.alertCycles = alertCycles;
        this.alertTimeoutMs = alertTimeoutMs;
        this.message = message;
        reset();
    }

    {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setJMenuBar(null);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        int width = toolkit.getScreenSize().width;
        int height = toolkit.getScreenSize().height;
        setBounds(0, 0, width, height);
        setAlwaysOnTop(true);
        setResizable(false);
        setLayout(new GridBagLayout());
    }

    private void reset() {
        if (isVisible()) {
            setVisible(false);
        }
        lables.forEach(this::remove);
        lables.clear();
        JTextArea label = new JTextArea();
        label.setCaret(new NoopCaret());
        label.setText(message);
        label.setEditable(false);
        label.setForeground(Color.RED);
        label.setBackground(Color.BLACK);
        label.setOpaque(true);
        label.setFont(font);
        lables.add(label);
        add(label);
    }

    public void start() {
        AudioAlertor.play();
        final Thread fontReplaces = new Thread(() -> {
            int cylce = 0;
            while (!Thread.currentThread().isInterrupted() && cylce < alertCycles) {
                cylce++;
                try {
                    Thread.sleep(alertTimeoutMs);
                    lables.forEach(l -> {
                        if (l.getForeground() == Color.RED) {
                            l.setForeground(Color.BLACK);
                            l.setBackground(Color.RED);
                        } else {
                            l.setForeground(Color.RED);
                            l.setBackground(Color.BLACK);
                        }
                    });
                    JFrameAlertor.this.repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        final Thread focurRequester = new Thread(() -> {
            int cylce = 0;
            while (!Thread.currentThread().isInterrupted() && cylce < alertCycles) {
                cylce++;
                try {
                    Thread.sleep(alertTimeoutMs);
                    JFrameAlertor.this.toFront();
                    setState(JFrame.NORMAL);
                    JFrameAlertor.this.requestFocusInWindow();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        setVisible(true);
        fontReplaces.start();
        focurRequester.start();
        try {
            focurRequester.join();
            fontReplaces.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setVisible(false);
        AudioAlertor.stop();
        setAlwaysOnTop(false);
        reset();
    }

    public void shutdown() {
        removeAll();
        dispose();
        setVisible(false);
    }
}
