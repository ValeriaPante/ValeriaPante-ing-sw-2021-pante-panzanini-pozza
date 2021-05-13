package it.polimi.ingsw.CustomDeserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;

import java.lang.reflect.Type;

public class PreGameDeserializer implements JsonDeserializer<InGameMessage> {
    @Override
    public InGameMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }
}
