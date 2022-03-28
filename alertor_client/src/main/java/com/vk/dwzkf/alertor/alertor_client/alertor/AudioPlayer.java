package com.vk.dwzkf.alertor.alertor_client.alertor;

import com.vk.dwzkf.alertor.alertor_client.AlertorClient;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
@Slf4j
public class AudioPlayer {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private byte[] audio;
    private Clip audioClip = null;
    private final String resourcePath;
    @Setter
    private boolean enabled = true;

    @PostConstruct
    public void init() {
        try {
            initAudio(resourcePath);
        } catch (Exception e) {
            audioClip = null;
            audio = null;
        }
    }

    public void initAudio(String path) {
        URL audioRes = AlertorClient.class.getClassLoader().getResource(path);
        if (audioRes == null) {
            log.warn("No audio found on path: {}", path);
            return;
        }
        try (InputStream in = audioRes.openConnection().getInputStream()) {
            audio = in.readAllBytes();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audio));
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioInputStream);
            audioInputStream.close();
            if (this.audioClip != null) {
                this.audioClip.stop();
                this.audioClip.close();
            }
            this.audioClip = audioClip;
        } catch (Exception e) {
            log.error("Cannot load sound.", e);
        }
    }

    public void play() {
        if (audioClip != null && enabled) {
            audioClip.stop();
            audioClip.setFramePosition(0);
            audioClip.setMicrosecondPosition(0);
            executorService.execute(audioClip::start);
        }
    }

    public void stop() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.setMicrosecondPosition(0);
            audioClip.setFramePosition(0);
        }
    }
}
