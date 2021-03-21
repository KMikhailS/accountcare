package ru.kmikhails.accountcare.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DataSource {
    private final HikariDataSource dataSource;

    public DataSource(String filename) {
        ResourceBundle resource = ResourceBundle.getBundle(filename);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(resource.getString("db.url"));
        config.setUsername(resource.getString("db.username"));
        config.setPassword(resource.getString("db.password"));
        config.setMaximumPoolSize(Integer.parseInt(resource.getString("db.poolsize")));
        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
