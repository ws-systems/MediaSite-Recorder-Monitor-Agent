package systems.whitestar.mediasite_monitor.Jobs;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import systems.whitestar.mediasite_monitor.Mediasite;
import systems.whitestar.mediasite_monitor.Models.Recorder;
import systems.whitestar.mediasite_monitor.Models.RecorderExpectation;
import systems.whitestar.mediasite_monitor.Models.Schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author Tom Paulus
 * Created on 5/9/18.
 */
@SuppressWarnings("unused")
@Log4j
public class RecorderExpectationCheck implements AgentJobInterface {
    private static final DateFormat ISO_DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final int TRIGGER_DELTA = 10;

    @Override public Map<String, String> execute(Map<String, String> payload) {
        RecorderExpectation expectation = new Gson().fromJson(payload.get("expectation"), RecorderExpectation.class);

        log.debug(expectation);

        // Verify that recording is still scheduled
        Schedule.Recurrence recurrence = Mediasite.getInstance().getRecurence(expectation.getScheduleId(), expectation.getRecurrenceId());
        if (!validRecurrence(recurrence)) {
            log.info("Schedule has changed since the expectation was scheduled. Skipping this check");
            payload.put("result", "skipped");
        }

        // Check Recorder Status
        Recorder recorder = Mediasite.getInstance().getRecorder(expectation.getRecorder().getId());
        if (recorder.getStatus() == expectation.getExpectedStatus()) {
            log.debug("Expectation Check Passed!");
            payload.put("result", "ok");
        } else {
            log.warn("Expectation Check Failed!");
            payload.put("result", "failed");
        }

        return payload;
    }

    private boolean validRecurrence(Schedule.Recurrence recurrence) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, -1 * TRIGGER_DELTA);
        final Date beforeBound = instance.getTime();

        instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, TRIGGER_DELTA);
        final Date afterBound = instance.getTime();

        Date recurrenceDate;

        try {
            recurrenceDate = ISO_DF.parse(recurrence.getNextScheduleTime());
        } catch (ParseException e) {
            log.warn(String.format("Problem parsing Recurrence date - \"%s\"", recurrence.getNextScheduleTime()), e);
            return false;
        }

        return recurrenceDate.after(beforeBound) && recurrenceDate.before(afterBound);
    }
}
