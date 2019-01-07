package UserStatusUpdate;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.text.SimpleDateFormat;
import java.util.*;

public class ScheduledTask extends TimerTask {

    private Server server;
    private String server_id;
    private DiscordApi api;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private Long userJoinDateInEpochMilli;
    private Date userJoinDate;
    private String userJoinDateFormatted;
    private Date endOfMonthDate;
    private String endOfMonthDateFormatted;

    protected ScheduledTask(String server_id, DiscordApi api ) {
        this.server_id = server_id;
        this.api = api;
    }

    public void run() {
        try {

            if(api.getServerById(server_id).isPresent()) {
                server = api.getServerById(server_id).get();
            }

            Collection<User> users = server.getMembers();

            users.forEach(user -> {
                if(user.getJoinedAtTimestamp(server).isPresent()) {

                    userJoinDateInEpochMilli = user.getJoinedAtTimestamp(server).get().toEpochMilli();

                    userJoinDate = new Date(userJoinDateInEpochMilli);

                    userJoinDateFormatted = format.format(userJoinDate);

                    Calendar addMonthToJoinDate = Calendar.getInstance();
                    addMonthToJoinDate.setTime(userJoinDate);
                    addMonthToJoinDate.add(Calendar.MONTH, +1);

                    endOfMonthDate = addMonthToJoinDate.getTime();
                    endOfMonthDateFormatted = format.format(endOfMonthDate);

                    if(userJoinDateFormatted.equalsIgnoreCase(endOfMonthDateFormatted)) {
                        Role regularRole = server.getRolesByNameIgnoreCase("Regular").get(0);
                        Role newcomerRole = server.getRolesByNameIgnoreCase("Newcomer").get(0);

                        server.removeRoleFromUser(user, newcomerRole);
                        server.addRoleToUser(user,regularRole);
                    }
                }
            });

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
