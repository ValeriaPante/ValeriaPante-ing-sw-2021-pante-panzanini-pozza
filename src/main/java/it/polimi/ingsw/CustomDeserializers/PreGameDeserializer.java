package it.polimi.ingsw.CustomDeserializers;

import com.google.gson.*;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.*;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;

import java.lang.reflect.Type;

public class PreGameDeserializer implements JsonDeserializer<PreGameMessage> {
    @Override
    public PreGameMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        try{
            switch (jsonObject.get("type").getAsString()){
                case "CreationLobby":
                    return new CreationLobbyMessage();
                case "Disconnect":
                    return new DisconnectMessage();
                case "MoveToLobby":
                    return new MoveToLobbyMessage(jsonObject.get("lobbyId").getAsInt());
                case "StartGame":
                    return new StartGameMessage();
                default:
                    //Error InGameMessage: "Incorrect message type for in-game"
                    return new NoActionMessage();
            }
        } catch (NullPointerException e) {
            //Error InGameMessage: "Incorrect message syntax"
            return new NoActionMessage();
        }
    }
}
