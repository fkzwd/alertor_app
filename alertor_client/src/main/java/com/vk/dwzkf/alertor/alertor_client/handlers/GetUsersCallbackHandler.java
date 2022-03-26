package com.vk.dwzkf.alertor.alertor_client.handlers;

import com.vk.dwzkf.alertor.alertor_client_core.handlers.EventHandler;
import com.vk.dwzkf.alertor.commons.socket_api.get_users.GetUsersCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetUsersCallbackHandler extends EventHandler<GetUsersCallback> {
    @Override
    public void handleEvent(GetUsersCallback event) {
        System.out.println("-".repeat(10));
        System.out.println("    USERS    ");
        event.getUsers().forEach(u -> {
            System.out.println(String.format("\tUser:%s Connected: true", u.getName()));
        });
        System.out.println("-".repeat(10));
    }
}
