package com.vk.dwzkf.alertor.commons.socket_api;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlertEvent {
    private String message;
    private Integer timeout;
    private Integer cycles;
}
