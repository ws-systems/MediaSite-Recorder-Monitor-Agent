package systems.whitestar.mediasite_monitor.Jobs;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import systems.whitestar.mediasite_monitor.Mediasite;
import systems.whitestar.mediasite_monitor.Models.Status;

import java.util.Map;

/**
 * @author Tom Paulus
 * Created on 5/14/17.
 */
@SuppressWarnings("unused")
@Log4j
public class SyncRecorderStatus implements AgentJobInterface {
    @Override public Map<String, String> execute(Map<String, String> payload) {
        String recorderID = payload.get("recorderID");
        log.info("Fetching Recorder Status for Recorder with ID: " + recorderID);

        Status status = null;

        try {
            status = Mediasite.getInstance().getRecorderStatus(Mediasite.getInstance().getRecorderIP(recorderID));
        } catch (RuntimeException e) {
            log.error("Problem retrieving recorder status from API - Invalid IP", e);
        }

        if (status == null) {
            log.error("Problem retrieving recorder status from API/Recorder");
            status = Status.UNKNOWN;
        }

        log.debug(String.format("Recorder Status is \"%s\"", status));
        payload.put("status", new Gson().toJson(status));
        return payload;
    }
}
