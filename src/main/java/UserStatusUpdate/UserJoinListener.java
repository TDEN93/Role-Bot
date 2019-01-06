package UserStatusUpdate;

import Database.AddUserToDB;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserJoinListener implements ServerMemberJoinListener {

    private Long currentDateEpochMilli;
    private String user_id;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private Date currentDate;
    private Server server;

    @Override
    public void onServerMemberJoin( ServerMemberJoinEvent event ) {

        try {

            server = event.getServer();

            if( event.getUser().getJoinedAtTimestamp(event.getServer()).isPresent() ) {
                currentDateEpochMilli = event.getUser().getJoinedAtTimestamp(event.getServer()).get().toEpochMilli();
            }

            user_id = event.getUser().getIdAsString();

            currentDate = new Date(currentDateEpochMilli);

            String userJoinDate = format.format(currentDate);

            Calendar addMonthToJoinDate = Calendar.getInstance();
            addMonthToJoinDate.setTime(currentDate);
            addMonthToJoinDate.add(Calendar.MONTH, +1);

            currentDate = addMonthToJoinDate.getTime();

            String userEndPeriod = format.format(currentDate);

            AddUserToDB addUserToDB = new AddUserToDB();
            addUserToDB.addUser(user_id, userJoinDate, userEndPeriod, server.getIdAsString());

            if( !server.getRolesByNameIgnoreCase("Newcomer").isEmpty() ) {
                Role role = server.getRolesByNameIgnoreCase("Newcomer").get(0);
                server.addRoleToUser(event.getUser(),role);
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
