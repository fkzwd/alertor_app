package com.vk.dwzkf.alertor.alertor_client.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vk.dwzkf.alertor.commons.configurators.ObjectMapperConfigurator;
import io.socket.emitter.Emitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public abstract class EventHandler<T> implements Emitter.Listener {
    private final ObjectMapper objectMapper = ObjectMapperConfigurator.configure(new ObjectMapper());

    @SuppressWarnings("unchecked")
    @Override
    public final void call(Object... args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("Cannot handle event cause no args");
        }

        try {
            Object arg = objectMapper.readValue(args[0].toString(), getParameterClass());
            handleEvent((T) arg);
        } catch (Exception e) {
            log.error("Error while mapping event from server.", e);
        }
    }

    private Class<?> getParameterClass() {
        Method[] allDeclaredMethods = ReflectionUtils.getAllDeclaredMethods(this.getClass());
        Method m = findMethod(allDeclaredMethods);
        return m.getParameterTypes()[0];
    }

    private Method findMethod(Method[] allDeclaredMethods) {
        return Arrays.stream(allDeclaredMethods)
                .filter(m -> m.getName().equalsIgnoreCase("handleEvent"))
                .filter(m -> m.getParameterCount() == 1)
                .filter(m -> m.getReturnType() == Void.TYPE)
                .filter(m -> m.getParameterTypes()[0] != Object.class)
                .findFirst()
                .orElseThrow();
    }

    public abstract void handleEvent(T event);
}
