package systems.whitestar.mediasite_monitor.Jobs;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import systems.whitestar.mediasite_monitor.Mediasite;
import systems.whitestar.mediasite_monitor.Models.RecorderExpectation;
import systems.whitestar.mediasite_monitor.Models.Schedule;
import systems.whitestar.mediasite_monitor.Models.Status;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Tom Paulus
 * Created on 5/9/18.
 */
@SuppressWarnings("unused")
@Log4j
public class ScheduleExpectationChecks implements AgentJobInterface {
    private static final DateFormat ISO_DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override public Map<String, String> execute(Map<String, String> payload) {
        log.info("Starting Schedule Expectation Check Scheduler Job");

        Schedule[] schedule = Mediasite.getInstance().getSchedule();
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        final Date startOfToday = instance.getTime();

        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        final Date endOfToday = instance.getTime();

        final List<RecorderExpectation> expectations = new ArrayList<>();

        if (schedule == null || schedule.length == 0) {
            log.info("Could not pull schedule from Mediasite, or it has no events - skipping job for this run");
            payload.put("expectations", new Gson().toJson(expectations));
            return payload;
        }

        for (Schedule s : schedule) {
            Schedule.Recurrence[] recurrences = Mediasite.getInstance().getRecurrences(s);
            if (recurrences == null || recurrences.length == 0) {
                log.info(String.format("Could not pull recurrences for schedule %s from Mediasite," +
                        "or it has no events - skipping job for this run", s.getId()));
                continue;
            }
            for (Schedule.Recurrence recurrence : recurrences) {
                try {
                    Date recurrenceDate = ISO_DF.parse(recurrence.getNextScheduleTime());
                    if (recurrenceDate.after(startOfToday) && recurrenceDate.before(endOfToday)) {
                        // Make list of today's scheduled recordings
                        expectations.add(RecorderExpectation.builder()
                                .recorder(Mediasite.getInstance().getRecorder(recurrence.getParentSchedule().getRecorderId()))
                                .expectationTime(recurrenceDate)
                                .expectedStatus(Status.RECORDING)
                                .scheduleId(recurrence.getParentSchedule().getId())
                                .recurrenceId(recurrence.getId())
                                .build());
                    } else {
                        log.debug(String.format("Skipping Recurrence %s for Schedule %s - not today", recurrence.getId(), s.getId()));
                    }
                } catch (ParseException e) {
                    log.warn(String.format("Unable to parse Next Occurrence for Recurrence ID: %s Schedule: %s", recurrence.getId(), s.getId()), e);
                }
            }
        }

        payload.put("expectations", new Gson().toJson(expectations));

        log.info("Finished Schedule Expectation Check Scheduler Job");

        return payload;
    }

}
