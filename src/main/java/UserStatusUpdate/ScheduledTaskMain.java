package UserStatusUpdate;

import org.javacord.api.DiscordApi;

import java.util.Timer;

public class ScheduledTaskMain {

    private String server_id;
    private DiscordApi api;

    public ScheduledTaskMain(String server_id, DiscordApi api) {
        this.server_id = server_id;
        this.api = api;
    }

    public void start() {

        Timer time = new Timer();
        ScheduledTask st = new ScheduledTask(server_id, api);

        time.schedule(st, Integer.parseInt(System.getenv("taskTime")), Integer.parseInt(System.getenv("taskTime")));
    }
}
