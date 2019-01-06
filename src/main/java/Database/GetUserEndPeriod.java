package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class GetUserEndPeriod {

    private PreparedStatement PreparedSQLStatement = null;
    private ResultSet resultSet = null;

    public List<String> getUserEndPeriod( String current_date ) throws SQLException, ClassNotFoundException {
        ConnectToDB connectToDB = new ConnectToDB();
        connectToDB.connect();

        List<String> users = new ArrayList<>();

        try {

            PreparedSQLStatement = connectToDB.connectToDatabase.prepareStatement("SELECT * from Users WHERE newcome_end_period=?;");
            PreparedSQLStatement.setString(1, current_date);

            resultSet = PreparedSQLStatement.executeQuery();

            while ( resultSet.next() ) {

                String userIDAsString = resultSet.getString("user_id");

                users.add(userIDAsString);
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return users;
    }
}
