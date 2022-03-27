package com.vk.dwzkf.alertor.commons.socket_api.users_state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserConnectCallback {
    private List<UserConnectData> connectStates;
}
