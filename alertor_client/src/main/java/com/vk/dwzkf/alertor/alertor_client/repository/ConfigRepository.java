package com.vk.dwzkf.alertor.alertor_client.repository;

import com.vk.dwzkf.alertor.alertor_client_core.config.SocketConfig;

public interface ConfigRepository {
    SocketConfig  getConfig();
    void saveConfig(SocketConfig socketConfig);
}
