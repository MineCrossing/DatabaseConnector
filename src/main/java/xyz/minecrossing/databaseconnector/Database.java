package xyz.minecrossing.databaseconnector;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {

    private final String ip, database, username, password;
    private final int port;

    private HikariDataSource hikari;
    private boolean isConnected = false;

    public Database(String ip, int port, String database, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        connect();
    }

    private void connect() {
        // create connection
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + database);
        config.addDataSourceProperty("useSSL", "false"); // stops console spam about ssl errors
        config.setUsername(username);
        if (password != null || password.contentEquals("")) config.setPassword(password);

        hikari = new HikariDataSource(config);

        // test connection
        try {
            Connection c = hikari.getConnection();
            c.close();
            isConnected = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() throws SQLException {
        return hikari.getConnection();
    }

    public void shutdown() {
        if (!hikari.isClosed()) hikari.close();
    }

    public boolean isConnected() {
        return isConnected;
    }

}
