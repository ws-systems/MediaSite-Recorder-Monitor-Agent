package systems.whitestar.mediasite_monitor.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import systems.whitestar.mediasite_monitor.Agent;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

/**
 * @author Tom Paulus
 * Created on 6/3/18.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentJob {
    private Class job;
    private Map<String, String> payload;
    private AgentJobStatus status;
    private String id;
    private Agent agent;
    private Timestamp created;
    private Timestamp updated;
    private int priority;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentJob job1 = (AgentJob) o;
        return getPriority() == job1.getPriority() &&
                Objects.equals(getJob(), job1.getJob()) &&
                Objects.equals(getPayload(), job1.getPayload()) &&
                getStatus() == job1.getStatus() &&
                Objects.equals(getId(), job1.getId()) &&
                Objects.equals(getAgent(), job1.getAgent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJob(), getPayload(), getStatus(), getId(), getAgent(), getPriority());
    }

    public enum AgentJobStatus {
        CREATED(0),
        RECEIVED(1),
        EXECUTED(2),
        DONE(3);

        @Getter
        private int status;

        AgentJobStatus(int status) {
            this.status = status;
        }
    }
}
