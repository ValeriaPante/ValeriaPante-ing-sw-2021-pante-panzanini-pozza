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

public class LocalMessageManager implements MessageManager{

    private final InGameControllerSwitch inGameControllerSwitch;

    public LocalMessageManager(View view) {
        Lobby lobby = new Lobby(0);
        lobby.addUser(new User("you", new LocalMessageSender(view)));
        this.inGameControllerSwitch = new InGameControllerSwitch(lobby, true);
    }

    @Override
    public void update(InGameMessage message) {
        message.readThrough(inGameControllerSwitch);
    }

    @Override
    public void update(PreGameMessage message) {
    }

    @Override
    public void connect(String ip, String port, String username) {

    }
}
