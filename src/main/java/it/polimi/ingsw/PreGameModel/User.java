package it.polimi.ingsw.PreGameModel;

import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;
import it.polimi.ingsw.Network.RequestHandlers.RequestHandler;
import it.polimi.ingsw.Network.Server.ConnectionHandler;
import it.polimi.ingsw.Network.Server.MessageSenderInterface;

public class User {

    private final String username;
    private final MessageSenderInterface connectionHandler;

    public User(String username, MessageSenderInterface connectionHandler){
        this.username = username;
        this.connectionHandler = connectionHandler;
    }

    public void send(FromServerMessage message){
        this.connectionHandler.send(message);
    }

    public int getId(){
        return this.connectionHandler.getId();
    }

    public void setId(int id){
        this.connectionHandler.setId(id);
    }

    public void setRequestHandler(RequestHandler requestHandler){
        this.connectionHandler.setRequestHandler(requestHandler);
    }

    public String getUsername(){
        return this.username;
    }
}
