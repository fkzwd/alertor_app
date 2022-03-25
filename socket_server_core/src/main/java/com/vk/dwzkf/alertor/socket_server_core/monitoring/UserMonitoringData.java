package com.vk.dwzkf.alertor.socket_server_core.monitoring;

import com.vk.dwzkf.alertor.commons.entity.UserData;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserMonitoringData {
    private UserData userData;
    private LocalDateTime connectTime;
    private LocalDateTime disconnectTime;
    private boolean connected;

    @Override
    public String toString() {
        return "UserMonitoringData{" +
                "uuid=" + userData.getUuid() +
                ", connectTime=" + connectTime +
                ", disconnectTime=" + disconnectTime +
                ", connected=" + connected +
                '}';
    }
}
