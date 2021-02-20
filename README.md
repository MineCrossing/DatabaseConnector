# DatabaseConnector
Database connection pooling utility and API for communication with databases between MineCrossing services.

DatabaseConnector uses connection pooling to simplify the process of managing multiple plugins connecting to the same database.

## Maven Repository
To use DatabaseConnector add these into your pom.xml
```xml
<repositories>
    ...
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
</repositories>

<dependencies>
    ...
    <dependency>
        <groupId>com.github.MineCrossing</groupId>
        <artifactId>DatabaseConnector</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

#### Example Usage
Note: Database calls should always be done asynchronously to avoid blocking the main thread.
```java
/**
 * Get a user's balance
 * 
 * @param user The user who's balance to get
 * @return The user's balance
*/
public long getMoney(User user) {
    long money = -1; // default money to -1

    try (Connection con = DatabaseConnector.getInstance().getConnection("database")) { // establish a connection with the database named "database"
        PreparedStatement ps = con.prepareStatement("SELECT money FROM money WHERE uuid = ? LIMIT 1;"); // create a prepared statement with a custom SQL query
        ps.setString(1, user.getUUID().toString()); // prepared statement params start at 1
            
        ResultSet rs = ps.executeQuery(); // execute the prepared statement query and store it in a ResultSet
        while (rs.next()) { // while the ResultSet has content
            money = rs.getLong("money"); // assign the money variable to be the value of the money column
        }
        ps.close(); // close the prepared statement
        rs.close(); // close the result set
        con.close(); // close the DB connection (IntelliJ wines, ignore it)
    } catch (SQLException e) {
        e.printStackTrace(); // catch any sql exceptions and print them to console
    }

    return money; // return the user's balance
}
``` 