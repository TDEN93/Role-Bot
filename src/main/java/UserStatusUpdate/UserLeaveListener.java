package UserStatusUpdate;

import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;

public class UserLeaveListener implements ServerMemberLeaveListener {

    @Override
    public void onServerMemberLeave( ServerMemberLeaveEvent event ) {
//
//        try {
//            RemoveUserFromDB removeUserFromDB = new RemoveUserFromDB();
//            removeUserFromDB.removeUserFromDBFromServer(event.getUser().getIdAsString(), event.getServer().getIdAsString());
//        } catch ( Exception e ) {
//            e.printStackTrace();
//        }
    }
}
