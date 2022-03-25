package com.vk.dwzkf.alertor.commons.socket_api;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlertCallback {
    private String message;
    private int timeout = 150;
    private int cycles = 30;
}
