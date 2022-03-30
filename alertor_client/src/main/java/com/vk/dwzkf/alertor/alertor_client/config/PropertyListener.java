package com.vk.dwzkf.alertor.alertor_client.config;

import java.util.List;
import java.util.Map;

public interface PropertyListener {
    List<String> properties();
    void onLoaded(Map<String, String> props);
    void onUpdated(String property, String value);
}
