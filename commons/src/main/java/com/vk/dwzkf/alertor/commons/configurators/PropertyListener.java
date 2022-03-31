package com.vk.dwzkf.alertor.commons.configurators;

import java.util.List;
import java.util.Map;

public interface PropertyListener {
    List<String> properties();
    void onLoaded(Map<String, String> props);
    void onUpdated(String property, String value);
}
