package UserStatusUpdate;

import Database.GetUserEndPeriod;
import Database.RemoveUserFromDB;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimerTask;
import java.util.Date;

public class ScheduledTask extends TimerTask {

    private Server server;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public ScheduledTask( Server server ) {
        this.server = server;
    }

    public void run() {

        Date currentDate = new Date();

        GetUserEndPeriod endPeriod = new GetUserEndPeriod();

        try {

            List<String> userIDs = endPeriod.getUserEndPeriod(format.format(currentDate));

            Role role = server.getRolesByNameIgnoreCase("Newcomer").get(0);

            if( !userIDs.isEmpty() ) {

                RemoveUserFromDB removeUserFromDB = new RemoveUserFromDB();

                int userIDLength = userIDs.size();

                for( int i = 0; i < userIDLength; i++ ) {
                    if (server.getMemberById(userIDs.get(i)).isPresent()) {
                        server.removeRoleFromUser(server.getMemberById(userIDs.get(i)).get(), role);
                        removeUserFromDB.removeUserFromDB(userIDs.get(i));
                    }
                }
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
