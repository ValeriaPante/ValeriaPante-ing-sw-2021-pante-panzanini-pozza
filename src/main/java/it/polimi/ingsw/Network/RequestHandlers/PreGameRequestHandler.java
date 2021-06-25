package it.polimi.ingsw.Network.RequestHandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Controller.PreGameControllerSwitch;
import it.polimi.ingsw.CustomDeserializers.PreGameDeserializer;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.*;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;
import it.polimi.ingsw.PreGameModel.User;
import it.polimi.ingsw.Network.Server.ConnectionHandler;

/**
 * Class that handles the incoming client's requests, when they are deciding the lobby to join, etc...
 */
public class PreGameRequestHandler implements RequestHandler{

    private final PreGameControllerSwitch preGameControllerSwitch;
    private final Gson customGson;

    /**
     * Constructor
     */
    public PreGameRequestHandler(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(PreGameMessage.class, new PreGameDeserializer());
        this.customGson = gsonBuilder.create();
        this.preGameControllerSwitch = new PreGameControllerSwitch(); //potrebbe essere sbagliato
    }

    /**
     * Request Evaluator
     * @param id id of the client that made this request
     * @param request request arrived
     */
    @Override
    public void requestEvaluator(int id, String request) {
        PreGameMessage message = customGson.fromJson(request, PreGameMessage.class);
        message.setSenderId(id);
        message.readThrough(preGameControllerSwitch);
    }

    /**
     * Connection crashed
     * @param id id of the crashed client
     */
    @Override
    public void connectionClosed(int id) {
        DisconnectMessage message = new DisconnectMessage();
        message.setSenderId(id);
        message.readThrough(preGameControllerSwitch);
    }

    //questo metodo lo chiamo sempre e solo tramite un thread
    /**
     * Connection of a new client
     * @param username of the new client
     * @param connectionHandler which connection handler must take care of the client requests
     */
    public void addNewSocket(String username, ConnectionHandler connectionHandler){
        this.preGameControllerSwitch.addNewUser(new User(username, connectionHandler));
    }

}
