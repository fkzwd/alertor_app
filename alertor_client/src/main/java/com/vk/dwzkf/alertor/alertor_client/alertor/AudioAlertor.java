package com.vk.dwzkf.alertor.alertor_client.alertor;

import com.vk.dwzkf.alertor.alertor_client.AlertorClient;
import com.vk.dwzkf.alertor.alertor_client.listener.AlertListener;
import com.vk.dwzkf.alertor.commons.socket_api.AlertCallback;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AudioAlertor implements AlertListener {
    private static byte[] audio = null;
    private static Clip audioClip = null;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final AlertConfig alertConfig;

    @Getter
    @Setter
    public static class AudioConfig {
        public static float volume = 1.0f;
        public static String path = "cs_1_6_go_go_go.wav";
        public static boolean enabled = true;

        public static void setVolume(float volume) {
            if (volume < 0f || volume >1.0f) {
                log.warn("Cannot set volume to {}", volume);
            }
            if (audioClip != null) {
                FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.VOLUME);
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range * volume) + gainControl.getMinimum();
                gainControl.setValue(gain);
                log.info("Set volume to {}, between {} and {}", gain, gainControl.getMinimum(), gainControl.getMaximum());
            }
        }
    }

    public static void setAudio(String path) {
        URL audioRes = AlertorClient.class.getClassLoader().getResource(path);
        if (audioRes == null) {
            log.warn("No audio found on path: {}", path);
        }
        try (InputStream in = audioRes.openConnection().getInputStream()) {
            audio = in.readAllBytes();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audio));
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioInputStream);
            audioInputStream.close();
            if (AudioAlertor.audioClip != null) {
                AudioAlertor.audioClip.stop();
                AudioAlertor.audioClip.close();
            }
            AudioAlertor.audioClip = audioClip;
        } catch (Exception e) {
            log.error("Cannot load sound.", e);
        }
    }

    public static void play() {
        if (audioClip != null && AudioConfig.enabled) {
            audioClip.start();
        }
    }

    public static void stop() {
        if (audioClip != null) {
            audioClip.stop();
            audioClip.setMicrosecondPosition(0);
            audioClip.setFramePosition(0);
        }
    }

    @Override
    public void onAlert(AlertCallback alertCallback) {
        if (alertConfig.isAudioEnabled()) {
            AudioAlertor.play();
            executorService.execute(() -> {
                try {
                    Thread.sleep(alertCallback.getTimeout() * alertCallback.getCycles());
                } catch (Exception e) {
                    log.error("Error while sleep.", e);
                } finally {
                    AudioAlertor.stop();
                }
            });
        }
    }
}
