package com.vk.dwzkf.alertor.commons.socket_api.get_users;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetUsersCallback {
    private List<UserData> users = new ArrayList<>();
}
