package UserStatusUpdate;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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

    private static boolean USE_DRY_RUN = Boolean.parseBoolean(System.getenv("USE_DRY_RUN"));


    protected ScheduledTask(String server_id, DiscordApi api ) {
        this.server_id = server_id;
        this.api = api;
    }

    public void run() {

        if(USE_DRY_RUN) {

            server = api.getServerById(server_id).get();
            System.out.println("\nDRY [Scheduled " + server.getName() + "]: Getting Server ID.. ");
            System.out.println("DRY [Scheduled " + server.getName() + "]: Server ID: " + server);


            newcomerRole = server.getRolesByNameIgnoreCase("Newcomer").get(0);
            regularRole = server.getRolesByNameIgnoreCase("Regular").get(0);
            sixMonthRole = server.getRolesByNameIgnoreCase("Pimp").get(0);
            yearRole = server.getRolesByNameIgnoreCase("Pimpest").get(0);

            System.out.println("\nDRY [Scheduled " + server.getName() + "]: Found Roles");

            // DRY RUN ASYNC
            System.out.println("\nDRY [Scheduled " + server.getName() + "]: Newcomer Async Started.. ");

            CompletableFuture.runAsync(() -> {
                try {
                    newcomerMembers = newcomerRole.getUsers();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }).thenRun(() -> {

                System.out.println("\nDRY [Scheduled " + server.getName() + "]: Getting Newcomers.. ");
                System.out.println("DRY [Scheduled " + server.getName() + "]: Newcomer Size: " +  newcomerMembers.size());

                newcomerMembers.forEach(member -> {

                    if(member.getJoinedAtTimestamp(server).isPresent()) {
                        System.out.println("\nDRY [Scheduled " + server.getName() + "]: Getting Member Join Date..");

                        userJoinDate = new Date(member.getJoinedAtTimestamp(server).get().toEpochMilli());
                    }

                    // One Month
                    addMonthToJoinDate.setTime(userJoinDate);
                    addMonthToJoinDate.add(Calendar.MONTH, +1);

                    endOfMonthPeriod = addMonthToJoinDate.getTime();
                    isMonthAfterJoinDate = currentDate.after(endOfMonthPeriod);

                    if(isMonthAfterJoinDate) {
                        System.out.println("\nDRY [Scheduled " + server.getName() + "]: If join date > month");
                        System.out.println("DRY [Scheduled " + server.getName() + "]: Removed Newcomer role from -> " + member.getName());
                        System.out.println("DRY [Scheduled " + server.getName() + "]: Added Regular role to -> " + member.getName());
                    }
                });

                System.out.println("\nDRY [Scheduled " + server.getName() + "]: Newcomer Async Finished ");
            });


            // Regular To Six Month
            System.out.println("\nDRY [Scheduled " + server.getName() + "]: Regular Async Started ");

            CompletableFuture.runAsync(() -> {
                try {
                    regularMembers = regularRole.getUsers();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).thenRun(() -> {
                System.out.println("\nDRY [Scheduled " + server.getName() + "]: Getting Regulars.. ");
                System.out.println("DRY [Scheduled " + server.getName() + "]: Regular Size: " +  regularMembers.size());


                regularMembers.forEach(member -> {

                    if(member.getJoinedAtTimestamp(server).isPresent()) {
                        System.out.println("\nDRY [Scheduled " + server.getName() + "]: Getting Member Join Date..");

                        userJoinDate = new Date(member.getJoinedAtTimestamp(server).get().toEpochMilli());
                    }

                    // Six Months
                    addSixMonthsToJoinDate.setTime(userJoinDate);
                    addSixMonthsToJoinDate.add(Calendar.MONTH, +6);

                    endOfSixMonthPeriod = addSixMonthsToJoinDate.getTime();
                    isSixMonthsAfterJoinDate = currentDate.after(endOfSixMonthPeriod);

                    if(isSixMonthsAfterJoinDate) {
                        System.out.println("\nDRY [Scheduled " + server.getName() + "]: If join date > 6 month");
                        System.out.println("DRY [Scheduled " + server.getName() + "]: Removed Regular role from -> " + member.getName());
                        System.out.println("DRY [Scheduled " + server.getName() + "]: Added SixMonth role to -> " + member.getName());

                    }
                });

                System.out.println("\nDRY [Scheduled " + server.getName() + "]: Regular Async Finished ");

            });

            // Six Month to One Year
            System.out.println("\nDRY [Scheduled " + server.getName() + "]: SixMonth Async Started ");


            CompletableFuture.runAsync(() -> {
                try {
                    sixMonthMembers = sixMonthRole.getUsers();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).thenRun(() -> {

                System.out.println("\nDRY [Scheduled " + server.getName() + "]: Getting SixMonths.. ");
                System.out.println("DRY [Scheduled " + server.getName() + "]: SixMonths Size: " +  sixMonthMembers.size());


                sixMonthMembers.forEach(member -> {

                    if(member.getJoinedAtTimestamp(server).isPresent()) {
                        System.out.println("\nDRY [Scheduled " + server.getName() + "]: Getting Member Join Date..");


                        userJoinDate = new Date(member.getJoinedAtTimestamp(server).get().toEpochMilli());
                    }

                    // One Year
                    addYearToJoinDate.setTime(userJoinDate);
                    addYearToJoinDate.add(Calendar.YEAR, +1);

                    endOfYearPeriod = addYearToJoinDate.getTime();
                    isYearAfterJoinDate = currentDate.after(endOfYearPeriod);

                    if(isYearAfterJoinDate) {
                        System.out.println("\nDRY [Scheduled " + server.getName() + "]: If join date > 1 Year");
                        System.out.println("DRY [Scheduled " + server.getName() + "]: Removed SixMonth role from -> " + member.getName());
                        System.out.println("DRY [Scheduled " + server.getName() + "]: Added YearRole role to -> " + member.getName());

                    }
                });

                System.out.println("\nDRY [Scheduled " + server.getName() + "]: SixMonth Async Finished ");
            });

        } else {

            // NON-DRY_RUN
            server = api.getServerById(server_id).get();

            System.out.println(server);

            newcomerRole = server.getRolesByNameIgnoreCase("Newcomer").get(0);
            regularRole = server.getRolesByNameIgnoreCase("Regular").get(0);
            sixMonthRole = server.getRolesByNameIgnoreCase("Pimp").get(0);
            yearRole = server.getRolesByNameIgnoreCase("Pimpest").get(0);

            // Newcomer to Regular
            CompletableFuture.runAsync(() -> {

                try {
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
        }
    }
}
