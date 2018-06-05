package systems.whitestar.mediasite_monitor.Models;

import com.google.gson.*;
import lombok.extern.log4j.Log4j;

import java.lang.reflect.Type;

/**
 * @author Tom Paulus
 * Created on 6/4/18.
 */
@Log4j
public class ClassTypeAdapter implements JsonSerializer<Class>, JsonDeserializer<Class> {
    @Override
    public JsonElement serialize(Class src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getName());
    }

    @Override
    public Class deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Class<?> clazz;
        String className = json.getAsString();

        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.warn(String.format("No class exists with name \"%s\"", className));
            throw new JsonSyntaxException(String.format("No class exists with name \"%s\"", className), e);
        }

        return clazz;
    }
}
