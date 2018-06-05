package systems.whitestar.mediasite_monitor.Jobs;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import systems.whitestar.mediasite_monitor.Mediasite;
import systems.whitestar.mediasite_monitor.Models.Recorder;

import java.util.Map;

/**
 * @author Tom Paulus
 * Created on 5/10/17.
 */
@SuppressWarnings("unused")
@Log4j
public class SyncRecorderDB implements AgentJobInterface {
    @Override public Map<String, String> execute(Map<String, String> payload) {
        log.info("Starting Recorder Sync Job");

        Recorder[] recorders = Mediasite.getInstance().getRecorders();
        if (recorders == null) {
            log.fatal("Problem retrieving recorder list from API");
            recorders = new Recorder[]{};
        }
        log.debug(String.format("Retrieved %d recorders from MS API", recorders.length));

        payload.put("recorders", new Gson().toJson(recorders));
        return payload;
    }
}
