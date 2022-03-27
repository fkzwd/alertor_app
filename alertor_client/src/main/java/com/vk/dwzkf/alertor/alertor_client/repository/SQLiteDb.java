package com.vk.dwzkf.alertor.alertor_client.repository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@Component
@Slf4j
public class SQLiteDb {
    public static final String TABLE_NAME = "config_table";
    @Getter
    private Connection connection;

    @PostConstruct
    public void init() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:client.db");
            try (PreparedStatement stm =
                         connection.prepareStatement("create table if not exists " + TABLE_NAME + "(" +
                    "id integer primary key, host text, port integer, context text, protocol text, name text)")) {
                stm.execute();
            }
        } catch ( Exception e ) {
            log.warn("Cannot connect to database.", e);
        }
        log.info("Opened database successfully");
    }
}
