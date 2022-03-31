package com.vk.dwzkf.alertor.commons.configurators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class PropertiesConfigurator {
    private Properties props;
    private final List<PropertyListener> listeners = new ArrayList<>();
    private final String propsFilePath;

    public void addListener(PropertyListener propertyListener) {
        listeners.add(propertyListener);
    }

    public void loadProps() {
        Path propsFile = Paths.get(propsFilePath);
        Properties props = new Properties();
        this.props = props;
        if (!Files.isRegularFile(propsFile)) {
            try {
                Files.createFile(propsFile);
            } catch (IOException e) {
                log.error("Cannot create props file", e);
                return;
            }
        }
        try (InputStream in = Files.newInputStream(propsFile)) {
            props.load(in);
        } catch (Exception e) {
            log.error("Cannot load properties from file: {}", propsFile.toAbsolutePath(), e);
            return;
        }
        listeners.forEach(l -> {
            List<String> keys = l.properties();
            Map<String, String> properties = props.entrySet().stream()
                    .filter(e -> keys.contains(e.getKey().toString()) && e.getValue() != null)
                    .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString()));
            try {
                if (!properties.isEmpty()) {
                    l.onLoaded(properties);
                }
            } catch (Exception exception) {
                log.error("Error on listener {}", l.getClass().getSimpleName(), exception);
            }
        });
    }

    public void saveProps() {
        final Path path = Paths.get(propsFilePath);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                log.error("Error on save props.", e);
                return;
            }
        }
        try (OutputStream out = Files.newOutputStream(path)) {
            props.store(out, null);
        } catch (Exception e) {
            log.error("Error on save props.", e);
        }
    }

    public void update(String key, String value) {
        props.put(Objects.requireNonNull(key), Objects.requireNonNull(value));
        listeners.stream()
                .filter(l -> l.properties().contains(key))
                .forEach(l -> l.onUpdated(key,value));
    }
}

