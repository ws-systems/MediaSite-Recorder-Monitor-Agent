package systems.whitestar.mediasite_monitor.Models;

import com.google.gson.GsonBuilder;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tom Paulus
 * Created on 5/10/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Recorder {
    @NonNull
    private String Id;

    @NonNull
    private String Name;

    @NonNull
    private String Description;

    @NonNull
    private String SerialNumber;

    @NonNull
    private String Version;

    @Getter
    private String WebServiceUrl;

    @NonNull
    private String LastVersionUpdateDate;

    @NonNull
    private String PhysicalAddress;

    @NonNull
    private String ImageVersion;

    private Status status;

    private Timestamp lastSeen;

    public Recorder(String id) {
        Id = id;
    }


    public Recorder(String id, Status status) {
        Id = id;
        this.status = status;
    }

    public String getIP() throws RuntimeException {
        if (this.getWebServiceUrl() == null || this.getWebServiceUrl().isEmpty()) {
            throw new RuntimeException("WebService URL not Defined");
        }

        Pattern pattern = Pattern.compile("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}");
        Matcher matcher = pattern.matcher(this.getWebServiceUrl());
        if (!matcher.find()) {
            throw new RuntimeException("No IP defined in WebService URL");
        }
        return matcher.group();
    }

    public String asJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new Status.StatusAdapterFactory());
        return builder.create().toJson(this);
    }

    public static Recorder merge(final Recorder existing, final Recorder newRecorder) {
        if (newRecorder.getId() != null && !newRecorder.getId().isEmpty()) {
            existing.Id = newRecorder.getId();
        }
        if (newRecorder.getName() != null && !newRecorder.getName().isEmpty()) {
            existing.Name = newRecorder.getName();
        }
        if (newRecorder.getDescription() != null && !newRecorder.getDescription().isEmpty()) {
            existing.Description = newRecorder.getDescription();
        }
        if (newRecorder.getSerialNumber() != null && !newRecorder.getSerialNumber().isEmpty()) {
            existing.SerialNumber = newRecorder.getSerialNumber();
        }
        if (newRecorder.getVersion() != null && !newRecorder.getVersion().isEmpty()) {
            existing.Version = newRecorder.getVersion();
        }
        if (newRecorder.getWebServiceUrl() != null && !newRecorder.getWebServiceUrl().isEmpty()) {
            existing.WebServiceUrl = newRecorder.getWebServiceUrl();
        }
        if (newRecorder.getLastVersionUpdateDate() != null && !newRecorder.getLastVersionUpdateDate().isEmpty()) {
            existing.LastVersionUpdateDate = newRecorder.getLastVersionUpdateDate();
        }
        if (newRecorder.getPhysicalAddress() != null && !newRecorder.getPhysicalAddress().isEmpty()) {
            existing.PhysicalAddress = newRecorder.getPhysicalAddress();
        }
        if (newRecorder.getImageVersion() != null && !newRecorder.getImageVersion().isEmpty()) {
            existing.ImageVersion = newRecorder.getImageVersion();
        }
        if (newRecorder.getLastSeen() != null ) {
            existing.lastSeen = newRecorder.getLastSeen();
        }
        if (newRecorder.getStatus() != null) {
            existing.status = newRecorder.getStatus();
        }

        return existing;
    }
}
