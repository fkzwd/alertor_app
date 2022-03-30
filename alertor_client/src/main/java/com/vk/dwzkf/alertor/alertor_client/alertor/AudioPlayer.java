package com.vk.dwzkf.alertor.alertor_client.alertor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class AudioPlayer {
    protected final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private byte[] audio;
    @Getter
    private Clip audioClip = null;
    private String resourcePath;
    private String filePath;
    @Getter
    protected final AudioConfig audioConfig = new AudioConfig(this);

    public AudioPlayer(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public AudioPlayer(Path filePath) {
        this.filePath = filePath.toAbsolutePath().toString();
    }

    @RequiredArgsConstructor
    public static class AudioConfig {
        private final AudioPlayer self;

        float volume = 1.0f;
        @Setter
        @Getter
        boolean enabled = true;

        public float getVolume() {
            if (self == null || self.getAudioClip() == null) return 0;
            FloatControl gainControl = (FloatControl) self.getAudioClip().getControl(FloatControl.Type.MASTER_GAIN);
            return (float) Math.pow(10f, gainControl.getValue() / 20f);
        }

        public void setVolume(float volume) {
            if (self == null || self.getAudioClip() == null) return;
            if (volume < 0f || volume > 1f)
                throw new IllegalArgumentException("Volume not valid: " + volume);
            FloatControl gainControl = (FloatControl) self.getAudioClip().getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume));
        }
    }

    @PostConstruct
    public void init() {
        try {
            reloadAudio();
        } catch (Exception e) {
            audioClip = null;
            audio = null;
        }
    }

    private void reloadAudio() {
        if (resourcePath == null) {
            reloadAudioFile(filePath);
        } else {
            reloadAudioResource(resourcePath);
        }
    }

    public void reloadAudioResource(String resourcePath) {
        try {
            reloadAudio(getUrl(resourcePath));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void reloadAudioFile(String filePath) {
        try {
            reloadAudio(Paths.get(filePath).toUri().toURL());
        } catch (Exception e) {
            log.error("Error occurred while load audio from path.", e);
        }
    }

    protected void reloadAudio(URL audioRes) {
        if (audioRes == null) {
            log.warn("No audio can be loaded.");
            return;
        }
        Clip prev = audioClip;
        if (audioClip != null) {
            audioClip.stop();
            audioClip.close();
            audioClip = null;
        }
        try (InputStream in = audioRes.openConnection().getInputStream()) {
            audio = in.readAllBytes();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audio));
            Clip audioClip = AudioSystem.getClip();
            audioClip.open(audioInputStream);
            audioInputStream.close();
            this.audioClip = audioClip;
        } catch (Exception e) {
            log.error("Cannot load sound.", e);
            audioClip = prev;
        }
    }

    public URL getUrl(String resourcePath) {
        if (resourcePath != null) {
            return this.getClass().getClassLoader().getResource(resourcePath);
        }
        return null;
    }

    public void play() {
        if (audioClip != null && getAudioConfig().enabled) {
            executorService.execute(()-> {
                audioClip.stop();
                audioClip.setFramePosition(0);
                audioClip.setMicrosecondPosition(0);
                audioClip.start();
            });
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
