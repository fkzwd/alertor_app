package com.vk.dwzkf.alertor.alertor_client.alertor;

import lombok.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AudioConfig {
    @Bean
    public AudioPlayer messagePlayer() {
        return new AudioPlayer("mixkit-correct-answer-tone-2870.wav");
    }
}
