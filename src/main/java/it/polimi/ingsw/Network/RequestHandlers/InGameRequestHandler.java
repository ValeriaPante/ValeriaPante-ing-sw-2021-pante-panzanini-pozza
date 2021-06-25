package it.polimi.ingsw.Network.RequestHandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.CustomDeserializers.InGameDeserializer;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;
import it.polimi.ingsw.PreGameModel.Lobby;

/**
 * Class that handles the incoming client's requests, when the game is started
 */
public class InGameRequestHandler implements RequestHandler{

    private final Gson customGson;
    private final InGameControllerSwitch inGameControllerSwitch;

    /**
     * Constructor
     * @param lobby the lobby reference of this request handler
     */
    public InGameRequestHandler(Lobby lobby){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(InGameMessage.class, new InGameDeserializer());
        this.customGson = gsonBuilder.create();
        this.inGameControllerSwitch = new InGameControllerSwitch(lobby, true);
    }

    /**
     * Request Evaluator
     * @param id id of the client that made this request
     * @param request request arrived
     */
    @Override
    public void requestEvaluator(int id, String request) {
        InGameMessage message = customGson.fromJson(request, InGameMessage.class);
        message.setSenderId(id);
        message.readThrough(inGameControllerSwitch);
    }

    /**
     * Connection crashed
     * @param id id of the crashed client
     */
    @Override
    public void connectionClosed(int id) {
        //da finire
    }
}
