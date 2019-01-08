package UserStatusUpdate;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ScheduledTask extends TimerTask {

    private Server server;
    private String server_id;
    private DiscordApi api;

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

    private Collection<User> newcomerMembers;
    private Collection<User> regularMembers;
    private Collection<User> sixMonthMembers;

    private Role newcomerRole;
    private Role regularRole;
    private Role sixMonthRole;
    private Role yearRole;

    protected ScheduledTask(String server_id, DiscordApi api ) {
        this.server_id = server_id;
        this.api = api;
    }

    public void run() {
        System.out.println("Async Begins");

        server = api.getServerById(server_id).get();

        System.out.println(server);

        newcomerRole = server.getRolesByNameIgnoreCase("Newcomer").get(0);
        regularRole = server.getRolesByNameIgnoreCase("Regular").get(0);
        sixMonthRole = server.getRolesByNameIgnoreCase("Pimp").get(0);
        yearRole = server.getRolesByNameIgnoreCase("Pimpest").get(0);

        // Newcomer to Regular

        CompletableFuture.runAsync(() -> {

            try {
                System.out.println("test");
                newcomerMembers = newcomerRole.getUsers();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }).thenRun(() -> {

            newcomerMembers.forEach(member -> {

                if(member.getJoinedAtTimestamp(server).isPresent()) {
                    userJoinDate = new Date(member.getJoinedAtTimestamp(server).get().toEpochMilli());
                }

                // One Month
                addMonthToJoinDate.setTime(userJoinDate);
                addMonthToJoinDate.add(Calendar.MONTH, +1);

                endOfMonthPeriod = addMonthToJoinDate.getTime();
                isMonthAfterJoinDate = currentDate.after(endOfMonthPeriod);

                if(isMonthAfterJoinDate) {
                    server.removeRoleFromUser(member, newcomerRole);
                    server.addRoleToUser(member,regularRole);
                }
            });
        });

        // Regular To Six Month
        CompletableFuture.runAsync(() -> {
                System.out.println("test2");
            try {
                regularMembers = regularRole.getUsers();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }).thenRun(() -> {
            regularMembers.forEach(member -> {

                if(member.getJoinedAtTimestamp(server).isPresent()) {
                    userJoinDate = new Date(member.getJoinedAtTimestamp(server).get().toEpochMilli());
                }

                // Six Months
                addSixMonthsToJoinDate.setTime(userJoinDate);
                addSixMonthsToJoinDate.add(Calendar.MONTH, +6);

                endOfSixMonthPeriod = addSixMonthsToJoinDate.getTime();
                isSixMonthsAfterJoinDate = currentDate.after(endOfSixMonthPeriod);

                if(isSixMonthsAfterJoinDate) {
                    server.removeRoleFromUser(member, regularRole);
                    server.addRoleToUser(member,sixMonthRole);
                }
            });
        });


        // Six Month to One Year
        CompletableFuture.runAsync(() -> {
            System.out.println("test3");
            try {
                sixMonthMembers = sixMonthRole.getUsers();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }).thenRun(() -> {
            sixMonthMembers.forEach(member -> {

                if(member.getJoinedAtTimestamp(server).isPresent()) {
                    userJoinDate = new Date(member.getJoinedAtTimestamp(server).get().toEpochMilli());
                }

                // One Year
                addYearToJoinDate.setTime(userJoinDate);
                addYearToJoinDate.add(Calendar.YEAR, +1);

                endOfYearPeriod = addYearToJoinDate.getTime();
                isYearAfterJoinDate = currentDate.after(endOfYearPeriod);

                if(isYearAfterJoinDate) {
                    server.removeRoleFromUser(member, sixMonthRole);
                    server.addRoleToUser(member,yearRole);
                }
            });
        });

        System.out.println("Async Ends");
    }
}
