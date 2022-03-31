package com.vk.dwzkf.alertor.updater;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;

public class Md5Computer {
    private final HttpClient httpClient = HttpClient.newBuilder().build();

    public String computMd5(String pathToJar) {
        try {
            Path path = Paths.get(pathToJar);
            MessageDigest md = MessageDigest.getInstance("md5");
            md.update(Files.readAllBytes(path));
            return new String(Base64.getEncoder().encode(md.digest()));
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public String getRemoteMd5(String address) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(address+"/alertor/md5"))
                    .build();
            HttpResponse<String> resp = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() == 200) {
                return resp.body();
            } else {
                System.err.println("Cannot load md5 hash, server return "+resp.statusCode());
                System.exit(1);
                return null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}
