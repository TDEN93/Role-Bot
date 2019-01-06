package UserStatusUpdate;

import org.javacord.api.entity.server.Server;

import java.util.Timer;

public class ScheduledTaskMain {

    private Server server;

    public ScheduledTaskMain( Server server ) {
        this.server = server;
    }

    public void start() throws InterruptedException {

        Timer time = new Timer();
        ScheduledTask st = new ScheduledTask(server);
        time.schedule(st, 1000, 1000);

    }
}
