package com.vk.dwzkf.alertor.alertor_client.alertor;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class AlertConfig {
    private boolean alertEnabled = true;
}
