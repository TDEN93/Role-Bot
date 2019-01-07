import UserStatusUpdate.ScheduledTaskMain;
import UserStatusUpdate.UserJoinListener;
import UserStatusUpdate.UserLeaveListener;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.*;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;


// run gradle build

public class Main {
    public static void main( String token, Context context ) {

        LambdaLogger logger = context.getLogger();

//        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        DiscordApi api = new DiscordApiBuilder().setToken(System.getenv("token")).login().join();

        Permissions permissions = new PermissionsBuilder().setState(PermissionType.MANAGE_ROLES, PermissionState.ALLOWED).build();

//        logger.log("Bot has successfully started");
//        logger.log("You can invite the bot by using the following url: " + api.createBotInvite(permissions));

        System.out.println("Bot has successfully started");
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite(permissions));

        api.addListener( new UserJoinListener() );
        api.addListener( new UserLeaveListener() );

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
