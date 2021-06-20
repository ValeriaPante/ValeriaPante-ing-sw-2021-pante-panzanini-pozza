package it.polimi.ingsw.Network.RequestHandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.CustomDeserializers.InGameDeserializer;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;
import it.polimi.ingsw.PreGameModel.Lobby;

public class InGameRequestHandler implements RequestHandler{

    private Gson customGson;
    private InGameControllerSwitch inGameControllerSwitch;

    public InGameRequestHandler(Lobby lobby){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(InGameMessage.class, new InGameDeserializer());
        this.customGson = gsonBuilder.create();
        this.inGameControllerSwitch = new InGameControllerSwitch(lobby, true);
    }

    @Override
    public void requestEvaluator(int id, String request) {
        InGameMessage message = customGson.fromJson(request, InGameMessage.class);
        message.setSenderId(id);
        message.readThrough(inGameControllerSwitch);
    }

    @Override
    public void connectionClosed(int id) {
        //da finire
    }
}
