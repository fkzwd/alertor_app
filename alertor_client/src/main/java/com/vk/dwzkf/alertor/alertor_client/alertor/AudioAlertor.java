package com.vk.dwzkf.alertor.alertor_client.alertor;

import com.vk.dwzkf.alertor.alertor_client.AlertorClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

@Slf4j
public class AudioAlertor {
    private static byte[] audio = null;
    private static Clip audioClip = null;

    @Getter
    @Setter
    public static class AudioConfig {
        public static float volume = 1.0f;
        public static String path = "skrillex_holdin_on_8.wav";
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
}
