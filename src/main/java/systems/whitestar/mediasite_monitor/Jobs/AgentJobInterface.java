package systems.whitestar.mediasite_monitor.Jobs;

import java.util.Map;

/**
 * @author Tom Paulus
 * Created on 6/3/18.
 */
public interface AgentJobInterface {
    Map<String,String> execute(Map<String,String> payload);
}
