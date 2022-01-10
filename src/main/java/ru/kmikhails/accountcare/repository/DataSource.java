package ru.kmikhails.accountcare.repository;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DataSource {
//    private final HikariDataSource dataSource;

    public DataSource(ResourceBundle resource) {
//        try {
//            Class.forName("org.sqlite.JDBC");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        HikariConfig config = new HikariConfig();
//        config.setDataSourceClassName("org.sqlite.JDBC");
//        config.setJdbcUrl(resource.getString("db.url"));
////        config.setUsername(resource.getString("db.username"));
////        config.setPassword(resource.getString("db.password"));
//        config.setMaximumPoolSize(Integer.parseInt(resource.getString("db.poolsize")));
//        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        File file = new File("accountcare.db");
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:file:" + file.getAbsolutePath());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
