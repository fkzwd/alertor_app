package com.vk.dwzkf.alertor.alertor_client.alertor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AudioConfig {
    public static List<String> DEFAULT_AUDIOS = List.of("skrillex_holdin_on_8.wav", "knife_party_give_it_up.wav", "cs_1_6_go_go_go.wav");
    private final AlertConfig alertConfig;


    @Bean
    public AudioPlayer messagePlayer() {
        return new AudioPlayer("mixkit-correct-answer-tone-2870.wav");
    }

    @Bean
    public AudioPlayer alertPlayer() {
        return new AudioAlertor(DEFAULT_AUDIOS.get(2), alertConfig);
    }
}
