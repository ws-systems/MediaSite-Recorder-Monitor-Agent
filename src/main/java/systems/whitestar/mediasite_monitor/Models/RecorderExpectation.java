package systems.whitestar.mediasite_monitor.Models;

import lombok.Builder;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Tom Paulus
 * Created on 5/9/18.
 */
@Data
@Builder
public class RecorderExpectation {
    private static final int TOLERANCE_KEY = Calendar.MINUTE;
    private static final int TOLERANCE_VALUE = 5;

    private Recorder recorder;
    private Status expectedStatus;
    private Date expectationTime;

    private String scheduleId;
    private Integer recurrenceId;

    public Date getCheckTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(expectationTime);
        calendar.add(TOLERANCE_KEY, TOLERANCE_VALUE);

        return calendar.getTime();
    }
}
