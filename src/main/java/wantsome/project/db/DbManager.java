package wantsome.project.db;

import org.sqlite.SQLiteConfig;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class which controls how the application connects to the used DB.
 * <p>
 * Holds all needed config details and provides the getConnection() method
 * to be called by rest of code when it needs to use the DB.
 */
public class DbManager {

    private static String dbFile = "hotel_reservation_project.db";

    /**
     * Sets the name of SQLite db file to be used.
     * MUST be called at least once before calling getConnection()
     * <p>
     * Note: we prefer to define this method instead of using just a constant
     * just because we want to be able to use a different file for tests
     * (so we don't corrupt the main/right db file)!
     */
    public static void setDbFile(String newDbFile) {
        dbFile = newDbFile;
        System.out.println("Using custom SQLite db file: " + new File(dbFile).getAbsolutePath());
    }

    /**
     * Provides a connection to the db.
     * <p>
     * TODO: this is very basic, and not very efficient. A better solution would
     * be for this method to use internally some kind of connection pool
     * (so it recycles the connections, would be faster/lighter in case of
     * performing many operations / requesting connections often)
     */
    public static Connection getConnection() throws SQLException {

        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true); //enable FK support (disabled by default)
        config.setDateStringFormat("yyyy-MM-dd HH:mm:ss"); //this also seems important, to avoid some problems with date/time fields..

        return DriverManager.getConnection("jdbc:sqlite:" + dbFile, config.toProperties());
    }
}
