package systems.whitestar.mediasite_monitor;

import systems.whitestar.mediasite_monitor.Models.AgentConfig;

/**
 * Register Agent with Central Server and retrieve saved configuration (Mediasite Credentials) for the agent.
 *
 * @author Tom Paulus
 * Created on 12/15/17.
 */
public class Register {
    static final int REGISTRATION_DELAY = 30; // How long to with between status checks
    private static boolean registrationRequestMade = false;

    static boolean registerAgent(final String agentID) {
        // Once the Initial Registration Request has been made (registrationRequestMade) skip the register
        // and just poll status

        // TODO
        return false;
    }

    static AgentConfig getConfig(final String agentID) {
        // TODO
        return null;
    }
}
