package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.Controller.PreGameControllerSwitch;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;
import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;
import it.polimi.ingsw.Network.Server.LocalMessageSender;
import it.polimi.ingsw.PreGameModel.Lobby;
import it.polimi.ingsw.PreGameModel.User;
import it.polimi.ingsw.View.View;

import java.util.ArrayList;

/**
 * Manages the messages from/to the player in an offline game
 */
public class LocalMessageManager implements MessageManager{

    private final InGameControllerSwitch inGameControllerSwitch;

    /**
     * Constructor
     * @param view view modality chosen by the player
     */
    public LocalMessageManager(View view) {
        Lobby lobby = new Lobby(0);
        User user = new User("you", new LocalMessageSender(view));
        user.setId(1);
        lobby.addUser(user);
        this.inGameControllerSwitch = new InGameControllerSwitch(lobby);
    }

    /**
     * Sends a message to the server
     * @param message message to send
     */
    @Override
    public void update(InGameMessage message) {
        message.readThrough(inGameControllerSwitch);
    }

    /**
     * Sends a message to the server
     * @param message message to send
     */
    @Override
    public void update(PreGameMessage message) {
    }

    /**
     * Starts the connection with the server if possible
     * @param ip IP address of the server
     * @param port port used by the server
     * @param username username chosen by the player
     */
    @Override
    public void connect(String ip, String port, String username) {

    }
}
