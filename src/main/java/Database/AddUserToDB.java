package Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddUserToDB {

    private PreparedStatement PreparedSQLStatement = null;

    public void addUser( String user_id, String join_date, String newcome_end_period ) throws SQLException, ClassNotFoundException {

        ConnectToDB connectToDB = new ConnectToDB();
        connectToDB.connect();

        try {

            PreparedSQLStatement = connectToDB.connectToDatabase.prepareStatement("INSERT INTO Users(user_id, join_date, newcome_end_period) VALUES(?, ?, ?);");

            PreparedSQLStatement.setString(1, user_id);
            PreparedSQLStatement.setString(2, join_date);
            PreparedSQLStatement.setString(3, newcome_end_period);

            PreparedSQLStatement.executeUpdate();

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
