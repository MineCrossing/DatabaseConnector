package xyz.minecrossing.databaseconnector;

import xyz.minecrossing.coreutilities.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConnector {

    private static final Map<String, Database> DATABASES = new HashMap<>();
    private static DatabaseConnector instance;

    /**
     * Constructor for creating database connections
     *
     * @param details The list of database connections to establish
     */
    private DatabaseConnector(List<DatabaseDetails> details) {
        for (DatabaseDetails detail : details) {
            Database database = new Database(detail);
            DATABASES.put(detail.getDatabase(), database);

            if (database.isConnected()) {
                Logger.info("Database '" + detail.getDatabase() + "' connected.");
            } else {
                Logger.error("Database '" + detail.getDatabase() + "' failed to connect!");
            }
        }
    }

    /**
     * Grab an instance of the database connector
     *
     * @return An instance of the database connector
     */
    public static DatabaseConnector getInstance() {
        if (instance == null) load();
        return instance;
    }

    /**
     * Load the database properties from file
     */
    private static void load() {
        // load databases from file using array for future proofing
        List<DatabaseDetails> detailsList = new ArrayList<>();

        DatabaseProperties properties = new DatabaseProperties();
        properties.createProperties(); // make sure file exists

        detailsList.add((DatabaseDetails) properties.loadProperties());

        // connect to databases
        instance = new DatabaseConnector(detailsList);
    }

    /**
     * Get a database connection dependant upon its ID
     *
     * @param database The database connection to grab
     * @return A database connection dependant upon its ID
     * @throws SQLException When the database does not exist
     */
    public Connection getConnection(String database) throws SQLException {
        if (DATABASES.containsKey(database)) return DATABASES.get(database).getConnection();
        return null;
    }

    /**
     * Manually add a database at run time
     *
     * @param details The detials of the database to add
     */
    public void addDatabase(DatabaseDetails details) {
        DATABASES.putIfAbsent(details.getDatabase(), new Database(details));
    }

    /**
     * Shutdown the connection to avoid memory leaks
     */
    public void shutdown() {
        for (Database database : DATABASES.values()) {
            if (database.isConnected()) database.shutdown();
        }
    }

}