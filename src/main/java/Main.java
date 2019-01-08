import UserStatusUpdate.BotJoinedServerListener;

import UserStatusUpdate.ScheduledTaskMain;
import UserStatusUpdate.UserJoinListener;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.*;

public class Main {

    private static boolean USE_DRY_RUN = Boolean.parseBoolean(System.getenv("USE_DRY_RUN"));
    public static void main(String[] args) {

        DiscordApi api = new DiscordApiBuilder().setToken(System.getenv("token")).login().join();

        Permissions permissions = new PermissionsBuilder().setState(PermissionType.MANAGE_ROLES, PermissionState.ALLOWED).build();

        System.out.println("Bot has successfully started");
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite(permissions));

        if(USE_DRY_RUN) {
            System.out.println("DRY: DRY RUN VARIABLE DETECTED, NO CHANGES WILL BE MADE\n");
        }

        api.addListener( new UserJoinListener());
        api.addServerJoinListener(new BotJoinedServerListener());

        api.getServers().forEach(server -> {

            ScheduledTaskMain scheduledTaskMain = new ScheduledTaskMain(server.getIdAsString(), api);

            try {
                scheduledTaskMain.start();
            } catch( Exception e ) {
                e.printStackTrace();
            }
        });
    }
}
