package com.vk.dwzkf.alertor.alertor_client.repository;

import com.vk.dwzkf.alertor.alertor_client_core.config.SocketConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConfigRepositoryImpl implements ConfigRepository {
    private final SQLiteDb db;

    @Override
    public SocketConfig getConfig() {
        try (PreparedStatement stm = db.getConnection().prepareStatement("select * from " + SQLiteDb.TABLE_NAME + ";");) {
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                String host = resultSet.getString("host");
                int port = resultSet.getInt("port");
                String name = resultSet.getString("name");
                String context = resultSet.getString("context");
                String protocol = resultSet.getString("protocol");
                return new SocketConfig(requireNonNull(host), requireNonNull(protocol), port, requireNonNull(context),requireNonNull(name));
            }
        } catch (Exception e) {
            log.error("Error while reading from db.", e);
        }
        return null;
    }

    @Override
    public void saveConfig(SocketConfig socketConfig) {
        try (PreparedStatement stm = db.getConnection().prepareStatement("delete from "+SQLiteDb.TABLE_NAME+" where true;")) {
            stm.execute();
        } catch (Exception e) {
            log.error("Error on sql.", e);
        }
        try (PreparedStatement stm = db.getConnection()
                .prepareStatement(
                        "insert into "+SQLiteDb.TABLE_NAME+"(id,name,host,port,context,protocol) " +
                                "values(?,?,?,?,?,?);")) {
            stm.setInt(1, 1);
            stm.setString(2, socketConfig.getName());
            stm.setString(3, socketConfig.getHost());
            stm.setInt(4,socketConfig.getPort());
            stm.setString(5,socketConfig.getContext());
            stm.setString(6,socketConfig.getProtocol());
            stm.execute();
        } catch (Exception e) {
            log.error("Error on sql.", e);
        }
    }
}
