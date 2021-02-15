package xyz.minecrossing.databaseconnector;

import xyz.minecrossing.coreutilities.remote.ConnectionDetails;

public class DatabaseDetails extends ConnectionDetails {

    private final String database;

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
