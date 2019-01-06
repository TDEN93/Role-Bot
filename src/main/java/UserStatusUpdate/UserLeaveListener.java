package UserStatusUpdate;

import Database.RemoveUserFromDB;
import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;

public class UserLeaveListener implements ServerMemberLeaveListener {

    @Override
    public void onServerMemberLeave( ServerMemberLeaveEvent event ) {

        try {

            RemoveUserFromDB removeUserFromDB = new RemoveUserFromDB();
            removeUserFromDB.removeUserFromDB(event.getUser().getIdAsString());

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
