package systems.whitestar.mediasite_monitor.Scheduler;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.log4j.Log4j;
import org.quartz.*;
import systems.whitestar.mediasite_monitor.Agent;
import systems.whitestar.mediasite_monitor.Models.AgentJob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Heartbeat job that runs every N seconds to check-in with the server. This serves two purposes: one to indicate that
 * the agent is still alive, and second to see if there are any new jobs for this agent to execute.
 *
 * @author Tom Paulus
 * Created on 6/4/18.
 */
@Log4j
public class Heartbeat implements Job {
    private static final String JOB_GROUP = "heartbeat";
    private static final String TRIGGER_NAME = "HeartbeatTrigger";
    private static final String JOB_NAME = "Heartbeat";

    /**
     * Schedule the Sync Job
     *
     * @param scheduler         {@link Scheduler} Quartz Scheduler Instance
     * @param intervalInSeconds How often the job should run in Seconds
     * @throws SchedulerException Something went wrong scheduling the job
     */
    public static void schedule(Scheduler scheduler, int intervalInSeconds) throws SchedulerException {
        JobDetail job = newJob(Heartbeat.class)
                .withIdentity(JOB_NAME, JOB_GROUP)
                .build();

        // Trigger the job to run now, and then repeat every X Minutes
        Trigger trigger = newTrigger()
                .withIdentity(TRIGGER_NAME, JOB_GROUP)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(intervalInSeconds)
                        .repeatForever())
                .startNow()
                .build();

        // Tell quartz to schedule the job using our trigger
        scheduler.scheduleJob(job, trigger);
    }

    /**
     * Check-in with the Web Server
     *
     * @return {@link AgentJob} Job to complete, if one is available, else null.
     */
    private static AgentJob ping() {
        HttpResponse<String> response;

        try {
            response = Unirest
                    .get(String.format("%s/agent/queue", Agent.getAgent().getServerURL()))
                    .asString();
        } catch (UnirestException e) {
            log.warn("Could not check in with Server - Check URL and config", e);
            throw new RuntimeException(e);
        }

        if (response.getStatus() == 204) {
            log.info("No jobs available for this agent, but check-in was acknowledged");
        } else if (response.getStatus() == 200) {
            AgentJob job = new Gson().fromJson(response.getBody(), AgentJob.class);
            log.info("Received job from Server");
            log.debug(job);
            return job;
        } else {
            log.warn("Error code received from server on check-in - HTTP Code: " + response.getStatus());
            log.debug(response.getBody());
        }

        return null;
    }

    /**
     * Push the job result to the web server for processing
     *
     * @param jobID {@link String} Job ID
     * @param payload {@link Map} Job Results
     */
    private static void pushResult(final String jobID, final Map<String, String> payload) {
        HttpResponse<String> response;

        try {
            response = Unirest
                    .post(String.format("%s/agent/job/%s", Agent.getAgent().getServerURL(), jobID))
                    .header("Content-Type", "application/json")
                    .body(new Gson().toJson(payload))
                    .asString();
        } catch (UnirestException e) {
            log.warn("Could not push result to server - Check URL and config", e);
            throw new RuntimeException(e);
        }

        if (response.getStatus() == 202) log.info("Job results accepted");
        else {
            log.warn("Job results rejected");
            log.debug(response.getBody());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.debug("❤");

        // Check in with Web Server
        AgentJob job = ping();
        if (job == null) return;

        // Execute a job if one was received
        Map<String, String> result = null;

        try {
            Class jobClass = job.getJob();
            Method method = jobClass.getMethod("execute", Map.class);
            result = (Map<String, String>) method.invoke(jobClass.newInstance(), job.getPayload());
        } catch (NoSuchMethodException e) {
            log.error("Class does not correctly implement the interface as it is missing the 'execute' method", e);
        } catch (IllegalAccessException | InstantiationException e) {
            log.error("Cannot instantiate new instance of job class", e);
        } catch (InvocationTargetException e) {
            log.error("Could not process job - method threw exception", e);
        }

        // Return the job result to the server
        pushResult(job.getId(), result);

        // Re-trigger Job to see if there is anything else
        try {
            Schedule.getScheduler().triggerJob(context.getJobDetail().getKey());
        } catch (SchedulerException e) {
            log.warn("Could not re-trigger heartbeat job", e);
        }
    }
}
