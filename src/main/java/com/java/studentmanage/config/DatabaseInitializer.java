package com.java.studentmanage.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        String url = context.getEnvironment().getProperty("spring.datasource.url");
        String username = context.getEnvironment().getProperty("spring.datasource.username");
        String password = context.getEnvironment().getProperty("spring.datasource.password");
        if (url == null || username == null) return;

        try {
            int paramIdx = url.indexOf('?');
            String cleanUrl = paramIdx > 0 ? url.substring(0, paramIdx) : url;
            int lastSlash = cleanUrl.lastIndexOf('/');
            if (lastSlash > 0) {
                String serverUrl = cleanUrl.substring(0, lastSlash);
                String dbName = cleanUrl.substring(lastSlash + 1);
                try (Connection conn = DriverManager.getConnection(serverUrl, username, password);
                     Statement stmt = conn.createStatement()) {
                    stmt.execute("CREATE DATABASE IF NOT EXISTS `" + dbName + "` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                }
            }
        } catch (Exception ignored) {
        }
    }
}
