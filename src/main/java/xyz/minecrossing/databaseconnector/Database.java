package xyz.minecrossing.databaseconnector;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {

    private final DatabaseDetails details;

    private HikariDataSource hikari;
    private boolean isConnected = false;

    /**
     * Constructor for creating a database class
     *
     * @param details The database connection details
     */
    public Database(DatabaseDetails details) {
        this.details = details;

        connect();
    }

    /**
     * Connect to the database using a connection pool
     */
    private void connect() {
        // create connection
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://" + details.getHostname() + ":" + details.getPort() + "/" + details.getDatabase());
        config.addDataSourceProperty("useSSL", "false"); // stops console spam about ssl errors
        config.setUsername(details.getUsername());

        String password = details.getPassword() == null ? "" : details.getPassword();
        config.setPassword(password);

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

    /**
     * Get the database connection from the connection pool
     *
     * @return A database connection from the connection pool
     * @throws SQLException When the pool does not exist
     */
    public Connection getConnection() throws SQLException {
        return hikari.getConnection();
    }

    /**
     * Shutdown the connection pool to avoid memory leaks
     */
    public void shutdown() {
        if (!hikari.isClosed()) hikari.close();
    }

    /**
     * Check that the database is connected
     *
     * @return <code>true</code> if the databsae is connected, <code>false</code> otherwise
     */
    public boolean isConnected() {
        return isConnected;
    }

}
