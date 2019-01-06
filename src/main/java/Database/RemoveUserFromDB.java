package Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RemoveUserFromDB  {

    private PreparedStatement PreparedSQLStatement = null;


    public void removeUserFromDBFromServer(String user_id, String server_id ) throws SQLException, ClassNotFoundException {

        ConnectToDB connectToDB = new ConnectToDB();
        connectToDB.connect();

        try {

            PreparedSQLStatement = connectToDB.connectToDatabase.prepareStatement("DELETE from Users WHERE user_id=? AND server_id=?;");

            PreparedSQLStatement.setString(1, user_id);
            PreparedSQLStatement.setString(2, server_id);

            PreparedSQLStatement.executeUpdate();

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void removeUserFromDBFromDate(String user_id, String currentDate ) throws SQLException, ClassNotFoundException {

        ConnectToDB connectToDB = new ConnectToDB();
        connectToDB.connect();

        try {

            PreparedSQLStatement = connectToDB.connectToDatabase.prepareStatement("DELETE from Users WHERE user_id=? AND newcome_end_period=?;");

            PreparedSQLStatement.setString(1, user_id);
            PreparedSQLStatement.setString(2, currentDate);

            PreparedSQLStatement.executeUpdate();

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
