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

    private static boolean USE_DRY_RUN = Boolean.parseBoolean(System.getenv("USE_DRY_RUN"));

    @Override
    public void onServerJoin(ServerJoinEvent event) {

        if(USE_DRY_RUN) {

            server = event.getServer();
            System.out.println("\nDRY [On Server Join]: Getting Server.. " + "\n" + "Server: " + server);

            Collection<User> discordMembers = server.getMembers();
            System.out.println("\nDRY [On Server Join]: Getting Members.. " + "Size: " + discordMembers.size());

            System.out.println("\nDRY [On Server Join]: Creating Roles..");

            RoleBuilder newcomerRoleBuilder = new RoleBuilder(server).setName("Newcomer").setDisplaySeparately(true).setColor(Color.WHITE);
            RoleBuilder regularRoleBuilder = new RoleBuilder(server).setName("Regular").setDisplaySeparately(true).setColor(Color.BLUE);
            RoleBuilder sixMonthRoleBuilder = new RoleBuilder(server).setName("Pimp").setDisplaySeparately(true).setColor(Color.PINK);
            RoleBuilder yearRoleBuilder = new RoleBuilder(server).setName("Pimpest").setDisplaySeparately(true).setColor(Color.YELLOW);

            if(server.getRolesByNameIgnoreCase("Newcomer").isEmpty()) {
                newcomerRoleBuilder.create();
            }

            if(server.getRolesByNameIgnoreCase("Regular").isEmpty()) {
                regularRoleBuilder.create();
            }

            if(server.getRolesByNameIgnoreCase("Pimp").isEmpty()) {
                sixMonthRoleBuilder.create();
            }

            if(server.getRolesByNameIgnoreCase("Pimpest").isEmpty()) {
                yearRoleBuilder.create().join();
            }


            newcomerRole = server.getRolesByNameIgnoreCase("Newcomer").get(0);
            regularRole = server.getRolesByNameIgnoreCase("Regular").get(0);
            sixMonthRole = server.getRolesByNameIgnoreCase("Pimp").get(0);
            yearRole = server.getRolesByNameIgnoreCase("Pimpest").get(0);

            System.out.println("\nDRY [On Server Join]: Created Role -> " + newcomerRole.getName());
            System.out.println("DRY [On Server Join]: Created Role -> " + regularRole.getName());
            System.out.println("DRY [On Server Join]: Created Role -> " + sixMonthRole.getName());
            System.out.println("DRY [On Server Join]: Created Role -> " + yearRole.getName());

            discordMembers.forEach(member -> {
                if(member.getJoinedAtTimestamp(server).isPresent()) {
                    System.out.println("\nDRY [On Server Join]: Getting Member Join Dates..");

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
                    System.out.println("\nDRY [On Server Join]: join date < month");
                    System.out.println("DRY [On Server Join]: Added Newcomer role to -> " + member.getName());

                } else if(isYearAfterJoinDate) {
                    System.out.println("\nDRY [On Server Join]: join date > year");
                    System.out.println("DRY [On Server Join]: Added year role to -> " + member.getName());

                } else if(isSixMonthsAfterJoinDate) {
                    System.out.println("\nDRY [On Server Join]: join date > 6 months");
                    System.out.println("DRY [On Server Join]: Added SixMonth role to -> " + member.getName());

                } else if(isMonthAfterJoinDate) {
                    System.out.println("\nDRY [On Server Join]: join date > month");
                    System.out.println("DRY [On Server Join]: Added regular role to -> " + member.getName());

                }
            });

            ScheduledTaskMain scheduledTaskMain = new ScheduledTaskMain(server.getIdAsString(), server.getApi());
            System.out.println("\nDRY [On Server Join]: Getting Server ID and API:");
            System.out.println("DRY [On Server Join]: Server ID: " + server.getIdAsString());
            System.out.println("DRY [On Server Join]: API: " + server.getApi());

            System.out.println("\nDRY [On Server Join]: Starting Scheduled Task");

            scheduledTaskMain.start();

        } else {
            server = event.getServer();

            Collection<User> discordMembers = server.getMembers();

            RoleBuilder newcomerRoleBuilder = new RoleBuilder(server).setName("Newcomer").setDisplaySeparately(true).setColor(Color.WHITE);
            RoleBuilder regularRoleBuilder = new RoleBuilder(server).setName("Regular").setDisplaySeparately(true).setColor(Color.BLUE);
            RoleBuilder sixMonthRoleBuilder = new RoleBuilder(server).setName("Pimp").setDisplaySeparately(true).setColor(Color.PINK);
            RoleBuilder yearRoleBuilder = new RoleBuilder(server).setName("Pimpest").setDisplaySeparately(true).setColor(Color.YELLOW);

            if(server.getRolesByNameIgnoreCase("Newcomer").isEmpty()) {
                newcomerRoleBuilder.create();
            }

            if(server.getRolesByNameIgnoreCase("Regular").isEmpty()) {
                regularRoleBuilder.create();
            }

            if(server.getRolesByNameIgnoreCase("Pimp").isEmpty()) {
                sixMonthRoleBuilder.create();
            }

            if(server.getRolesByNameIgnoreCase("Pimpest").isEmpty()) {
                yearRoleBuilder.create().join();
            }

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

            ScheduledTaskMain scheduledTaskMain = new ScheduledTaskMain(server.getIdAsString(), server.getApi());

            scheduledTaskMain.start();
        }
    }
}
