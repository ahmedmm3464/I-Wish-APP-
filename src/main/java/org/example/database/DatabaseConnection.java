package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;

    private String url;
    private String user;
    private String password;
    private Connection connection;
    private final String DB_ROOT ="jdbc:mysql://127.0.0.1:3306/iwish_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private DatabaseConnection() {
        this.url = setting(
                "iwish.db.url",
                "IWISH_DB_URL",
                DB_ROOT

        );

        this.user = setting(
                "iwish.db.user",
                "IWISH_DB_USER",
                "root"
        );

        this.password = setting(
                "iwish.db.pass",
                "IWISH_DB_PASS",
                "m961575M"
        );
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) instance = new DatabaseConnection();
        return instance;
    }

    public synchronized void configure(String url, String user, String password) {
        disconnect();
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public synchronized Connection connect() throws SQLException {

        if (connection == null || connection.isClosed()) {

            System.out.println("DB User: " + user);
            System.out.println("Password empty: " + password.isEmpty());

            connection = DriverManager.getConnection(
                    url,
                    user,
                    password
            );
        }

        return connection;
    }

    public Connection getConnection() throws SQLException { return connect(); }

    public synchronized void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Could not close the database connection: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }

    private static String setting(String prop, String env, String def) {
        String v = System.getProperty(prop);
        if (v == null) v = System.getenv(env);
        return v != null ? v : def;
    }
}
