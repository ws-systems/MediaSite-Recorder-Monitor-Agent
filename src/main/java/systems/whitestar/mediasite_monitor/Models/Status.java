package systems.whitestar.mediasite_monitor.Models;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Getter;

import java.io.IOException;

/**
 * Mediasite Recorder Status Codes Mapping
 *
 * @author Tom Paulus
 * Created on 7/3/17.
 */
@SuppressWarnings("unused")
public enum Status {
    UNKNOWN(-1, "Unknown"),
    UNAVAILABLE(1, "Unavailable"),
    IDLE(2, "Idle"),
    BUSY(3, "Busy"),
    RECORDING(4, "Recording"),
    PRE_RECORD(5, "Before Record"),
    POST_RECORD(6, "After Record"),
    PAUSED(7, "Paused"),
    INCOMPATIBLE(8, "Incompatible"),
    ERROR(9, "Unknown Error"),
    ERROR_INFO(10, "Error Additional Info"),
    DETAILS_DEVICE(11, "Details Devices"),
    MONITOR(12, "Monitor"),
    COULD_NOT_DELETE_ALL_FORMAT(13, "Could Not Delete All Format"),
    COULD_NOT_LICENSE(14, "Could Not License"),
    LICENSE_INITIATED(15, "License Initiated"),
    UPDATE_LOGIN_ERROR(16, "Update Login Error"),
    UPDATE_ERROR(17, "Update Error"),
    UPDATE_SUCCESS(18, "Update Success"),
    UPDATE_REMOVE(19, "Update Remove");

    private @Getter
    int stateCode;
    private @Getter
    String stateString;

    Status(int stateCode, String stateString) {
        this.stateCode = stateCode;
        this.stateString = stateString;
    }

    public static Status getByCode(int statusCode) {
        for (Status s : values()) {
            if (s.stateCode == statusCode) {
                return s;
            }
        }
        return Status.UNKNOWN;
    }

    public static Status getByName(String name) {
        for (Status s : values()) {
            if (s.stateString.equals(name)) {
                return s;
            }
        }
        return Status.UNKNOWN;
    }

    public boolean okay() {
        return 1 <= this.getStateCode() && this.getStateCode() <= 6;
    }

    public boolean inAlarm() {
        return !okay();
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class StatusAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
            Class<? super T> rawType = type.getRawType();
            if (rawType == Status.class) {
                return new StatusAdapter<>();
            }
            return null;
        }

        public class StatusAdapter<T> extends TypeAdapter<T> {

            public void write(JsonWriter out, T value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                Status status = (Status) value;
                // Here write what you want to the JsonWriter.
                out.beginObject();
                out.name("string");
                out.value(status.getStateString());
                out.name("code");
                out.value(status.getStateCode());
                out.endObject();
            }

            public T read(JsonReader in) throws IOException {
                // Properly deserialize the input (if you use deserialization)
                return null;
            }
        }
    }
}
