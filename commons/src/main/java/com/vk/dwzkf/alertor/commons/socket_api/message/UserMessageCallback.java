package com.vk.dwzkf.alertor.commons.socket_api.message;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMessageCallback {
    private String message;
    private UserData userData;
    private boolean yourMessage;
    private LocalDateTime time;

    public UserMessageCallback(String message, UserData userData) {
        this.message = message;
        this.userData = userData;
    }

    public UserMessageCallback(String message) {
        this.message = message;
    }
}
