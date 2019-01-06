package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToDB {

    public Connection connectToDatabase = null;

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connectToDatabase = DriverManager.getConnection("jdbc:sqlite:72Pin.db");
    }
}
