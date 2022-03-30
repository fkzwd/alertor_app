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
public class AudioAlertor extends AudioPlayer implements AlertListener{
    private final AlertConfig alertConfig;

    public AudioAlertor(String resourcePath, AlertConfig alertConfig) {
        super(resourcePath);
        this.alertConfig = alertConfig;
    }

    @Override
    public void onAlert(AlertCallback alertCallback) {
        if (alertConfig.isAudioEnabled()) {
            play();
            executorService.execute(() -> {
                try {
                    Thread.sleep(alertCallback.getTimeout() * alertCallback.getCycles());
                } catch (Exception e) {
                    log.error("Error while sleep.", e);
                } finally {
                    stop();
                }
            });
        }
    }
}
