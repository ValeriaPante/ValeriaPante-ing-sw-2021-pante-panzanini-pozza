package it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages;

import it.polimi.ingsw.Controller.PreGameControllerSwitch;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;

public class CreationLobbyMessage extends PreGameMessage {
    @Override
    public void readThrough(PreGameControllerSwitch preGameControllerSwitch) {
        preGameControllerSwitch.actionOnMessage(this);
    }
}
