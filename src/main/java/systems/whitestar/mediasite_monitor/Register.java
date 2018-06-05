package systems.whitestar.mediasite_monitor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.log4j.Log4j;
import systems.whitestar.mediasite_monitor.Models.AgentConfig;

/**
 * Register Agent with Central Server and retrieve saved configuration (Mediasite Credentials) for the agent.
 *
 * @author Tom Paulus
 * Created on 12/15/17.
 */
@Log4j
public class Register {
    static final int REGISTRATION_DELAY = 30; // How long to with between status checks
    private static boolean registrationRequestMade = false;

    static boolean registerAgent() {
        // Once the Initial Registration Request has been made (registrationRequestMade) skip the register
        // and just poll status

        if (!registrationRequestMade) {
            boolean result = makeRegistrationRequest();
            registrationRequestMade = true;
            return result;
        } else {
            return checkStatus();
        }
    }

    static AgentConfig getConfig() {
        HttpResponse<String> response;
        try {
            response = Unirest
                    .get(String.format("%s/agent/config", Agent.getAgent().getServerURL()))
                    .asString();

        } catch (UnirestException e) {
            log.warn("Could not get Agent Config from Server - Check URL and config", e);
            throw new RuntimeException(e);
        }

        return new Gson().fromJson(response.getBody(), AgentConfig.class);
    }

    private static boolean makeRegistrationRequest() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        HttpResponse<String> response;
        try {
            response = Unirest
                    .post(String.format("%s/agent/register", Agent.getAgent().getServerURL()))
                    .header("Content-Type", "application/json")
                    .body(gson.toJson(Agent.getAgent()))
                    .asString();

        } catch (UnirestException e) {
            log.warn("Could not register Agent with Server - Check URL and config", e);
            throw new RuntimeException(e);
        }

        Agent responseAgent = gson.fromJson(response.getBody(), Agent.class);
        return responseAgent != null && responseAgent.isAuthorized();
    }

    private static boolean checkStatus() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        HttpResponse<String> response;
        try {
            response = Unirest
                    .get(String.format("%s/agent/register/status/%s", Agent.getAgent().getServerURL(), Agent.getAgent().getId()))
                    .asString();
        } catch (UnirestException e) {
            log.warn("Could not check Agent Status with Server - Check URL and config", e);
            throw new RuntimeException(e);
        }

        Agent responseAgent = gson.fromJson(response.getBody(), Agent.class);
        return responseAgent != null && responseAgent.isAuthorized();

    }
}
