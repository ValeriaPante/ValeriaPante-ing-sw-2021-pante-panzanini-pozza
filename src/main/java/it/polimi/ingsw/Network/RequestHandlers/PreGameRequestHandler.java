package it.polimi.ingsw.Network.RequestHandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.Controller.PreGameControllerSwitch;
import it.polimi.ingsw.CustomDeserializers.PreGameDeserializer;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.*;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;
import it.polimi.ingsw.PreGameModel.User;
import it.polimi.ingsw.Network.Server.ConnectionHandler;

public class PreGameRequestHandler implements RequestHandler{

    private PreGameControllerSwitch preGameControllerSwitch;
    private Gson customGson;

    public PreGameRequestHandler(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(PreGameMessage.class, new PreGameDeserializer());
        this.customGson = gsonBuilder.create();
        this.preGameControllerSwitch = new PreGameControllerSwitch(true); //potrebbe essere sbagliato
    }

    @Override
    public void requestEvaluator(int id, String request) {
        PreGameMessage message = customGson.fromJson(request, PreGameMessage.class);
        message.setSenderId(id);
        message.readThrough(preGameControllerSwitch);
    }

    @Override
    public void connectionClosed(int id) {
        DisconnectMessage message = new DisconnectMessage();
        message.setSenderId(id);
        message.readThrough(preGameControllerSwitch);
    }

    //questo metodo lo chiamo sempre e solo tramite un thread
    public void addNewSocket(String username, ConnectionHandler connectionHandler){
        this.preGameControllerSwitch.addNewUser(new User(username, connectionHandler));
    }

}
