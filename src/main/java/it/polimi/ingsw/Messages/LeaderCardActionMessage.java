package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Controller.ControllerSwitch;

public class LeaderCardActionMessage extends BooleanIntMessage{
    public LeaderCardActionMessage(int integer, boolean aBoolean) {
        super(integer, aBoolean);
    }

    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
