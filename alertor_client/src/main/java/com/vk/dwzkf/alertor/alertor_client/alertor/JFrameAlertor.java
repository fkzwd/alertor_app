package com.vk.dwzkf.alertor.alertor_client.alertor;

import com.vk.dwzkf.alertor.alertor_client.AlertorClient;
import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class JFrameAlertor extends JFrame {
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private final java.util.List<JLabel> lables = new ArrayList<>();
    private final int alertCycles;
    private final int alertTimeoutMs;
    private static final Font font = new Font(Font.SERIF, Font.BOLD, 35);
    private final String message;
    private static byte[] audio = null;
    private static Clip audioClip = null;

    static {
        loadAudio();
    }

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
        setAlwaysOnTop(false);
        setResizable(false);
        setLayout(new GridBagLayout());
    }

    private static void loadAudio() {
        URL audioRes = AlertorClient.class.getClassLoader().getResource("skrillex_holdin_on_8.wav");
        try (InputStream in = audioRes.openConnection().getInputStream()) {
            audio = in.readAllBytes();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audio));
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioInputStream);
            audioClip.getMicrosecondLength();
            JFrameAlertor.audioClip = audioClip;
            audioInputStream.close();
        } catch (Exception e) {
            log.error("Cannot load sound.", e);
        }
    }

    private void reset() {
        if (isVisible()) {
            setVisible(false);
        }
        lables.forEach(this::remove);
        lables.clear();
        JLabel label = new JLabel(message.repeat(20));
        label.setForeground(Color.RED);
        label.setBackground(Color.BLACK);
        label.setOpaque(true);
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        lables.add(label);
        add(label);
    }

    public void start() {
        Clip clip = playSound();
        final Thread fontReplaces = new Thread(() -> {
            int cylce = 0;
            while (!Thread.currentThread().isInterrupted() && cylce < alertCycles) {
                cylce++;
                try {
                    Thread.sleep(alertTimeoutMs);
                    lables.forEach(l -> {
                        if (l.getForeground() == Color.RED) {
                            l.setForeground(Color.BLACK);
                            JFrameAlertor.this.repaint();
                            l.setBackground(Color.RED);
                            JFrameAlertor.this.repaint();
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
                    Thread.sleep(15);
                    requestFocus();
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
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.setMicrosecondPosition(0);
        }
        reset();
    }

    private Clip playSound(){
        if (audioClip == null) return null;
        try {
            audioClip.start();
            return audioClip;
        } catch (Exception e) {
            log.error("Cannot play sound.", e);
            return null;
        }
    }

    public void shutdown() {
        removeAll();
        dispose();
        setVisible(false);
    }
}
