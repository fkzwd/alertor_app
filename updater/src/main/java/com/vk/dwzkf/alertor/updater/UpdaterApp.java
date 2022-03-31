package com.vk.dwzkf.alertor.updater;

import com.vk.dwzkf.alertor.commons.configurators.PropertiesConfigurator;
import com.vk.dwzkf.alertor.commons.configurators.PropertyListener;

import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

public class UpdaterApp implements PropertyListener {
    private final PropertiesConfigurator propertiesConfigurator = new PropertiesConfigurator("updater.props");
    private String address;
    private String pathToJar;
    private final Md5Computer md5Computer = new Md5Computer();
    private final UpdateLoader updateLoader = new UpdateLoader();

    public static void main(String[] args) {
        new UpdaterApp().run();
    }

    public void run() {
        propertiesConfigurator.addListener(this);
        propertiesConfigurator.loadProps();
        //onLoaded hook was not executed cause no props found
        System.err.println("Error after load: address or/and path_to_jar not configured.");
        System.exit(1);
    }

    @Override
    public List<String> properties() {
        return List.of("address", "path_to_jar");
    }

    @Override
    public void onLoaded(Map<String, String> props) {
        String addr = props.get("address");
        String pathToJar = props.get("path_to_jar");
        if (addr == null || pathToJar == null) {
            System.err.println("Error null detected: address or/and path_to_jar not configured.");
            System.exit(-1);
        }
        this.pathToJar = pathToJar;
        this.address = addr;
        try {
            int code = checkForUpdates();
            if (code == 0) {
                System.out.println("Updated or already actual version.");
            }
            System.exit(code);
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    private int checkForUpdates() throws Exception {
        String currentmd5 = md5Computer.computMd5(pathToJar);
        String remoteMd5 = md5Computer.getRemoteMd5(address);
        if (currentmd5.equals(remoteMd5)) {
            return 0;
        }
        Path tmp = Files.createTempFile(null, null);
        try {
            Files.write(tmp, updateLoader.load(address));
            Files.move(tmp, Paths.get(pathToJar), StandardCopyOption.REPLACE_EXISTING);
            return 0;
        } finally {
            Files.deleteIfExists(tmp);
        }
    }

    @Override
    public void onUpdated(String property, String value) {

    }
}
