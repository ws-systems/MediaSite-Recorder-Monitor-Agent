package systems.whitestar.mediasite_monitor.Models;

import lombok.*;

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
}
