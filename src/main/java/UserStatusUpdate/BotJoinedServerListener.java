package UserStatusUpdate;

import org.javacord.api.entity.permission.RoleBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.ServerJoinEvent;
import org.javacord.api.listener.server.ServerJoinListener;
import org.javacord.api.entity.permission.Role;

import java.awt.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class BotJoinedServerListener implements ServerJoinListener {

    private Server server;
    private Role newcomerRole;
    private Role regularRole;
    private Role sixMonthRole;
    private Role yearRole;

    private Date userJoinDate;
    private Date currentDate = new Date();

    private Calendar addYearToJoinDate = Calendar.getInstance();
    private Calendar addSixMonthsToJoinDate = Calendar.getInstance();
    private Calendar addMonthToJoinDate = Calendar.getInstance();

    private Date endOfYearPeriod;
    private Date endOfSixMonthPeriod;
    private Date endOfMonthPeriod;

    private boolean isYearAfterJoinDate;
    private boolean isSixMonthsAfterJoinDate;
    private boolean isMonthAfterJoinDate;
    private boolean isMonthBeforeJoinDate;

    @Override
    public void onServerJoin(ServerJoinEvent event) {

        System.out.println("On Server Listener Start");

        server = event.getServer();

        Collection<User> discordMembers = server.getMembers();

        RoleBuilder newcomerRoleBuilder = new RoleBuilder(server).setName("Newcomer").setDisplaySeparately(true).setColor(Color.WHITE);
        RoleBuilder regularRoleBuilder = new RoleBuilder(server).setName("Regular").setDisplaySeparately(true).setColor(Color.BLUE);
        RoleBuilder sixMonthRoleBuilder = new RoleBuilder(server).setName("Pimp").setDisplaySeparately(true).setColor(Color.PINK);
        RoleBuilder yearRoleBuilder = new RoleBuilder(server).setName("Pimpest").setDisplaySeparately(true).setColor(Color.YELLOW);

        newcomerRoleBuilder.create();
        regularRoleBuilder.create();
        sixMonthRoleBuilder.create();
        yearRoleBuilder.create().join();

        newcomerRole = server.getRolesByNameIgnoreCase("Newcomer").get(0);
        regularRole = server.getRolesByNameIgnoreCase("Regular").get(0);
        sixMonthRole = server.getRolesByNameIgnoreCase("Pimp").get(0);
        yearRole = server.getRolesByNameIgnoreCase("Pimpest").get(0);

        discordMembers.forEach(member -> {
            if(member.getJoinedAtTimestamp(server).isPresent()) {
                userJoinDate = new Date(member.getJoinedAtTimestamp(server).get().toEpochMilli());
            }

            // One Month
            addMonthToJoinDate.setTime(userJoinDate);
            addMonthToJoinDate.add(Calendar.MONTH, +1);

            endOfMonthPeriod = addMonthToJoinDate.getTime();
            isMonthBeforeJoinDate = currentDate.before(endOfMonthPeriod);
            isMonthAfterJoinDate = currentDate.after(endOfMonthPeriod);

            // Six Month
            addSixMonthsToJoinDate.setTime(userJoinDate);
            addSixMonthsToJoinDate.add(Calendar.MONTH, +6);

            endOfSixMonthPeriod = addSixMonthsToJoinDate.getTime();
            isSixMonthsAfterJoinDate = currentDate.after(endOfSixMonthPeriod);

            // One Year
            addYearToJoinDate.setTime(userJoinDate);
            addYearToJoinDate.add(Calendar.YEAR, +1);

            endOfYearPeriod = addYearToJoinDate.getTime();
            isYearAfterJoinDate = currentDate.after(endOfYearPeriod);

            if(isMonthBeforeJoinDate) {
                server.addRoleToUser(member,newcomerRole);
                System.out.println("Added Newcomer");
            } else if(isYearAfterJoinDate) {
                server.addRoleToUser(member,yearRole);
                System.out.println("Added Pimpest");
            } else if(isSixMonthsAfterJoinDate) {
                server.addRoleToUser(member,sixMonthRole);
                System.out.println("Added Pimp");
            } else if(isMonthAfterJoinDate) {
                server.addRoleToUser(member,regularRole);
                System.out.println("Added Regular");
            }
        });

        System.out.println("On Server Listener End");

        ScheduledTaskMain scheduledTaskMain = new ScheduledTaskMain(server.getIdAsString(), server.getApi());

        System.out.println("Starting from listerner");
        scheduledTaskMain.start();
    }
}
