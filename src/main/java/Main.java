import SQLITE.DiscordDB;

import UserStatusUpdate.ScheduledTaskMain;
import UserStatusUpdate.UserJoinListener;
import UserStatusUpdate.UserLeaveListener;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.*;

public class Main {

    private static DiscordDB discordTableInDataBase = new DiscordDB();
    private static String token = discordTableInDataBase.getDiscordAuthToken();

    public static void main( String[] args ) {

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        Permissions permissions = new PermissionsBuilder().setState(PermissionType.MANAGE_ROLES, PermissionState.ALLOWED).build();

        System.out.println("Bot has successfully started");
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite(permissions));

        api.addListener( new UserJoinListener() );
        api.addListener( new UserLeaveListener() );

        api.getServers().forEach(server -> {

            ScheduledTaskMain scheduledTaskMain = new ScheduledTaskMain(server);

            try {
                scheduledTaskMain.start();
            } catch( Exception e ) {
                e.printStackTrace();
            }
        });
    }
}
