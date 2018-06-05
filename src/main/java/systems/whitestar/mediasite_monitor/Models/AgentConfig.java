package systems.whitestar.mediasite_monitor.Models;

import lombok.Data;

/**
 * @author Tom Paulus
 * Created on 12/15/17.
 */
@Data
public class AgentConfig {
    private String url;
    private String apiKey;
    private String apiUser;
    private String apiPass;
}
