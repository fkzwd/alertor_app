package com.vk.dwzkf.alertor.commons.socket_api.message;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage {
    private String message;
    private UserData userData;
}
