package it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages;

import it.polimi.ingsw.Controller.PreGameControllerSwitch;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;

public class MoveToLobbyMessage extends PreGameMessage {
    private final int lobbyId;

    public MoveToLobbyMessage(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public int getLobbyId() {
        return lobbyId;
    }

    @Override
    public void readThrough(PreGameControllerSwitch preGameControllerSwitch) {
        preGameControllerSwitch.actionOnMessage(this);
    }
}
