package UserStatusUpdate;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;


public class UserJoinListener implements ServerMemberJoinListener {

    private Server server;

    @Override
    public void onServerMemberJoin( ServerMemberJoinEvent event ) {

        try {

            server = event.getServer();

            if( !server.getRolesByNameIgnoreCase("Newcomer").isEmpty() ) {
                Role role = server.getRolesByNameIgnoreCase("Newcomer").get(0);
                server.addRoleToUser(event.getUser(),role);
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
