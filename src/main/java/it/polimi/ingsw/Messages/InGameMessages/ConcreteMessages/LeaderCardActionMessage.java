package it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages;

import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.Messages.InGameMessages.BooleanIntMessage;

public class LeaderCardActionMessage extends BooleanIntMessage {
    public LeaderCardActionMessage(int integer, boolean aBoolean) {
        super(integer, aBoolean);
    }

    public void readThrough(InGameControllerSwitch inGameControllerSwitch){
        inGameControllerSwitch.actionOnMessage(this);
    }
}
