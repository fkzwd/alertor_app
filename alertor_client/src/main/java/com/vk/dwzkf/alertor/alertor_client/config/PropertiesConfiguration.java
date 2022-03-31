package com.vk.dwzkf.alertor.alertor_client.config;

import com.vk.dwzkf.alertor.commons.configurators.PropertiesConfigurator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class PropertiesConfiguration {
    public static final String PROPS_PATH = "./app.props";

    @Bean
    public PropertiesConfigurator propertiesConfigurator() {
        return new PropertiesConfigurator(PROPS_PATH);
    }

    @EventListener(ApplicationStartedEvent.class)
    public void onStart() {
        propertiesConfigurator().loadProps();
    }

    @EventListener(ContextClosedEvent.class)
    public void onExit() {
        propertiesConfigurator().saveProps();
    }
}
