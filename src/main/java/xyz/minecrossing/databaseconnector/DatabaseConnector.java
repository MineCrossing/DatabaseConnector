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

    public static void main(String[] args) {
        // load databases from file using array for future proofing
        List<DatabaseDetails> detailsList = new ArrayList<>();

        DatabaseProperties properties = new DatabaseProperties();
        properties.createProperties(); // make sure file exists

        detailsList.add((DatabaseDetails) properties.loadProperties());

        // connect to databases
        instance = new DatabaseConnector(detailsList);
    }

    private DatabaseConnector(List<DatabaseDetails> details) {
        for (DatabaseDetails detail : details) {
            Database database = new Database(detail.getHostname(), detail.getPort(), detail.getDatabase(), detail.getUsername(), detail.getPassword());
            DATABASES.put(detail.getDatabase(), database);

            if (database.isConnected()) {
                Logger.info("Database '" + detail.getDatabase() + "' connected.");
            } else {
                Logger.error("Database '" + detail.getDatabase() + "' failed to connect!");
            }
        }
    }

    public static DatabaseConnector getInstance() {
        return instance;
    }

    public Connection getConnection(String database) throws SQLException {
        if (DATABASES.containsKey(database)) return DATABASES.get(database).getConnection();
        return null;
    }

    public void shutdown() {
        for (Database database : DATABASES.values()) {
            if (database.isConnected()) database.shutdown();
        }
    }

}