package xyz.minecrossing.databaseconnector;

import xyz.minecrossing.coreutilities.remote.ConnectionDetails;

public class DatabaseDetails extends ConnectionDetails {

    private final String database;

    /**
     * Constructor for creating database details
     *
     * @param hostname The hostname of the databse server
     * @param port     The port of the database server
     * @param database The database to use on the server
     * @param username The username of the database user
     * @param password The password of the database user
     */
    public DatabaseDetails(String hostname, int port, String database, String username, String password) {
        super(hostname, port, username, password);
        this.database = database;
    }

    /**
     * Get the name of the selected database
     *
     * @return The name of the selected database
     */
    public String getDatabase() {
        return database;
    }
}
