package it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages;

import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.Messages.InGameMessages.IntMessage;

public class MoveToLeaderStorageMessage extends IntMessage {
    public MoveToLeaderStorageMessage(int integer) {
        super(integer);
    }

    public void readThrough(InGameControllerSwitch inGameControllerSwitch){
        inGameControllerSwitch.actionOnMessage(this);
    }
}
