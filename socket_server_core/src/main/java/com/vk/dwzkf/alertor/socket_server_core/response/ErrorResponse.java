package com.vk.dwzkf.alertor.socket_server_core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private String detailedMessage;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
