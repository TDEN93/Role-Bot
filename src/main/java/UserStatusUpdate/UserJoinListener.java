package UserStatusUpdate;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

public class UserJoinListener implements ServerMemberJoinListener {

    private Server server;
    private static boolean USE_DRY_RUN = Boolean.parseBoolean(System.getenv("USE_DRY_RUN"));

    @Override
    public void onServerMemberJoin( ServerMemberJoinEvent event ) {

        try {
            if(USE_DRY_RUN) {

                System.out.println("\nDRY [New Member]: Getting Server.. ");
                System.out.println("\nDRY [New Member]: Server: " + event.getServer());

                server = event.getServer();

                if( !server.getRolesByNameIgnoreCase("Newcomer").isEmpty() ) {
                    System.out.println("\nDRY [New Member]: Server Has Newcomer Role: " + !server.getRolesByNameIgnoreCase("Newcomer").isEmpty());

                    Role role = server.getRolesByNameIgnoreCase("Newcomer").get(0);

                    System.out.println("\nDRY [New Member]: Added Newcomer Role -> " + event.getUser().getName());

                }

            } else {
                server = event.getServer();

                if( !server.getRolesByNameIgnoreCase("Newcomer").isEmpty() ) {
                    Role role = server.getRolesByNameIgnoreCase("Newcomer").get(0);
                    server.addRoleToUser(event.getUser(),role);
                }
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
