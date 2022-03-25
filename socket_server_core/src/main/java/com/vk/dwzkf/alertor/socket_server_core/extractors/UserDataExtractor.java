package com.vk.dwzkf.alertor.socket_server_core.extractors;

import com.vk.dwzkf.alertor.commons.entity.UserData;

public interface UserDataExtractor<T> {
    UserData extract(T from);
}
