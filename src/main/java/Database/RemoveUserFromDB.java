package Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveUserFromDB  {

    private PreparedStatement PreparedSQLStatement = null;

    public void removeUserFromDB( String user_id ) throws SQLException, ClassNotFoundException {

        ConnectToDB connectToDB = new ConnectToDB();
        connectToDB.connect();

        try {

            PreparedSQLStatement = connectToDB.connectToDatabase.prepareStatement("DELETE from Users WHERE user_id=?;");

            PreparedSQLStatement.setString(1, user_id);

            PreparedSQLStatement.executeUpdate();

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
