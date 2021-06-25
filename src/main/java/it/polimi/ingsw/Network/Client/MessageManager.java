package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;

/**
 * Manages the messages from the player
 */
public interface MessageManager {

    /**
     * Sends a message to the server
     * @param message message to send
     */
    void update(InGameMessage message);

    /**
     * Sends a message to the server
     * @param message message to send
     */
    void update(PreGameMessage message);

    /**
     * Starts the connection with the server if possible
     * @param ip IP address of the server
     * @param port port used by the server
     * @param username username chosen by the player
     */
    void connect(String ip, String port, String username);
}
