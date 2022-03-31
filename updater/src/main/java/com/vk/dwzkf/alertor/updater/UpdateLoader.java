package com.vk.dwzkf.alertor.updater;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UpdateLoader {
    private final HttpClient httpClient = HttpClient.newBuilder().build();

    public byte[] load(String address) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(address+"/alertor/jar"))
                    .build();
            HttpResponse<byte[]> resp = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
            if (resp.statusCode() == 200) {
                return resp.body();
            } else {
                System.err.println("Cannot load jar file, server return "+resp.statusCode());
                System.exit(1);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}
