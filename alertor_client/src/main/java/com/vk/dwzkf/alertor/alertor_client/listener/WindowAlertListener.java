package com.vk.dwzkf.alertor.alertor_client.listener;

import com.vk.dwzkf.alertor.alertor_client.alertor.AlertConfig;
import com.vk.dwzkf.alertor.alertor_client.alertor.JFrameAlertor;
import com.vk.dwzkf.alertor.commons.socket_api.AlertCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
@RequiredArgsConstructor
public class WindowAlertListener extends JFrame implements AlertListener {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final AlertConfig alertConfig;

    @Override
    public void onAlert(AlertCallback alertCallback) {
        log.info("Alert received. Message: {}", alertCallback.getMessage());
        if (alertConfig.isAlertEnabled()) {
            executorService.execute(() -> {
                JFrameAlertor alertor = new JFrameAlertor(alertCallback.getCycles(),
                        alertCallback.getTimeout(),
                        alertCallback.getMessage()
                );
                alertor.start();
                alertor.shutdown();
            });
        }
    }
}
