package it.polimi.ingsw.PreGameModel;

import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;
import it.polimi.ingsw.Network.RequestHandlers.RequestHandler;
import it.polimi.ingsw.Network.Server.MessageSenderInterface;

/**
 * Representation of a user
 */
public class User {

    private final String username;
    private final MessageSenderInterface connectionHandler;

    /**
     * Constructor
     * @param username username of this user
     * @param connectionHandler the handler of this user requests
     */
    public User(String username, MessageSenderInterface connectionHandler){
        this.username = username;
        this.connectionHandler = connectionHandler;
    }

    /**
     * Sending a message
     * @param message message to send
     */
    public void send(FromServerMessage message){
        this.connectionHandler.send(message);
    }

    /**
     * Getter
     * @return return this player id
     */
    public int getId(){
        return this.connectionHandler.getId();
    }

    /**
     * Setter
     * @param id sets this user id
     */
    public void setId(int id){
        this.connectionHandler.setId(id);
    }

    /**
     * Update the handler of this user requests
     * @param requestHandler the new handler of this user requests
     */
    public void setRequestHandler(RequestHandler requestHandler){
        this.connectionHandler.setRequestHandler(requestHandler);
    }

    /**
     * Getter
     * @return this user username
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Closes the connection associated to this user
     */
    public void closeConnection(){
        this.connectionHandler.closeConnection();
    }
}
