package com.vk.dwzkf.alertor.alertor_client.listener;

import com.vk.dwzkf.alertor.commons.socket_api.AlertCallback;

public interface AlertListener {
    void onAlert(AlertCallback alertCallback);
}

