package systems.whitestar.mediasite_monitor;

import lombok.extern.log4j.Log4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Manage Scheduler Instance
 *
 * @author Tom Paulus
 * Created on 2/3/17.
 */
@Log4j
public class Schedule {
    private static Scheduler scheduler = null;

    public static Scheduler getScheduler() {
        try {
            if (scheduler == null) {
                scheduler = StdSchedulerFactory.getDefaultScheduler();
            }
        } catch (SchedulerException e) {
            log.fatal("Problem Initializing Scheduler", e);
        }

        return scheduler;
    }
}
