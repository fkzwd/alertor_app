package com.vk.dwzkf.alertor.alertor_client.config;

import com.vk.dwzkf.alertor.commons.configurators.PropertiesConfigurator;
import com.vk.dwzkf.alertor.commons.configurators.PropertyListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PropertiesRegistry {
    private final PropertiesConfigurator propertiesConfigurator;

    @Autowired(required = false)
    public void registerListeners(List<PropertyListener> listeners) {
        listeners.forEach(propertiesConfigurator::addListener);
    }
}
