package it.polimi.ingsw.Messages.InGameMessages;

import it.polimi.ingsw.Controller.ControllerSwitch;
import it.polimi.ingsw.Messages.BooleanIntMessage;

public class LeaderCardActionMessage extends BooleanIntMessage {
    public LeaderCardActionMessage(int integer, boolean aBoolean) {
        super(integer, aBoolean);
    }

    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
