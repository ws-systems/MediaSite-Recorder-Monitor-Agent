package systems.whitestar.mediasite_monitor;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.quartz.SchedulerException;
import systems.whitestar.mediasite_monitor.Models.AgentConfig;
import systems.whitestar.mediasite_monitor.Scheduler.Heartbeat;
import systems.whitestar.mediasite_monitor.Scheduler.Schedule;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Entry point for Agent Executable.
 *
 * @author Tom Paulus
 * Created on 12/15/17.
 */
@Log4j
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Agent {
    private static final String ID_FILE_PATH = "/var/lib/ms-mon-agent/config.properties";
    private static final int HEARTBEAT_RATE = 30;  // How often, in seconds, that the agent should check-in with the server
    private static final int EXECUTION_DELAY = 60; // How often the agent thread should check if there are still jobs running

    @Getter
    private static Agent agent;
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose(serialize = false)
    private boolean authorized;
    private String serverURL;

    public static void main(String[] args) {
        String serverURL = System.getenv("MS-MON-SERVER-URL");
        String agentName = System.getenv("MS-MON-AGENT-NAME");
        String agentID = getAgentID();

        if (agentID == null || agentID.isEmpty()) {
            log.warn("First Run - Generating Agent ID");
            agentID = generateAgentID();
            log.info("Generated Agent ID - " + agentID);
            saveAgentID(agentID);
        }

        agent = Agent.builder()
                .id(agentID)
                .serverURL(serverURL)
                .name(agentName)
                .build();

        log.debug("Starting Agent");
        agent.start();

        try {
            Schedule.getScheduler().start();
        } catch (SchedulerException e) {
            log.error("Could not start Scheduler", e);
            throw new RuntimeException(e);
        }


        try {
            do {
                try {
                    TimeUnit.SECONDS.sleep(EXECUTION_DELAY);
                } catch (InterruptedException e) {
                    log.warn("Agent Interrupted", e);
                }
            } while (Schedule.getScheduler().getTriggerGroupNames().size() > 0);
        } catch (SchedulerException e) {
            log.error("Problem getting active Job Triggers", e);
            throw new RuntimeException(e);
        }
    }

    private static String getAgentID() {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(ID_FILE_PATH));
        } catch (IOException e) {
            log.debug("Properties File not found", e);
            log.warn("Could not locate agent id file. A new Agent ID will be generated.");
            return null;
        }

        return properties.getProperty("agent-id");
    }

    private static String generateAgentID() {
        return UUID.randomUUID().toString();
    }

    private static void saveAgentID(final String id) {
        log.info("Saving Agent ID to " + ID_FILE_PATH);
        Properties properties = new Properties();
        properties.setProperty("agent-id", id);

        try {
            properties.store(new FileOutputStream(ID_FILE_PATH),
                    "Mediasite Recorder Agent Configuration - Updated " +
                            ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        } catch (IOException e) {
            log.fatal("Cannot Save Agent ID to Disk - Agent will not stay authorized between restarts");
            log.warn("File Path - " + ID_FILE_PATH);
        }
    }

    private void start() {
        log.info("Registering Agent");
        log.debug("Agent ID = " + this.id);
        log.debug("Agent Name = " + this.name);
        log.debug("Server URL = " + this.serverURL);

        boolean authorized;
        do {
            authorized = Register.registerAgent();
            if (!authorized) {
                try {
                    TimeUnit.SECONDS.sleep(Register.REGISTRATION_DELAY);
                } catch (InterruptedException e) {
                    log.warn("Could not pause for Agent Registration Status Refresh", e);
                }
            }
        } while (!authorized);


        log.info("Getting Agent Configuration");
        AgentConfig config = Register.getConfig();
        if (config == null) throw new RuntimeException("Config cannot be null");

        try {
            Mediasite.init(config);
        } catch (InstantiationException e) {
            log.error("Could not init Mediasite API Connector", e);
            throw new RuntimeException(e);
        }

        try {
            Heartbeat.schedule(Schedule.getScheduler(), HEARTBEAT_RATE);
        } catch (SchedulerException e) {
            log.fatal("Could not schedule heartbeat job", e);
            throw new RuntimeException(e);
        }
    }
}
