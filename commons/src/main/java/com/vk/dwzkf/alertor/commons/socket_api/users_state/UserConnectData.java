package com.vk.dwzkf.alertor.commons.socket_api.users_state;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserConnectData {
    private boolean connected;
    private UserData userData;
}
