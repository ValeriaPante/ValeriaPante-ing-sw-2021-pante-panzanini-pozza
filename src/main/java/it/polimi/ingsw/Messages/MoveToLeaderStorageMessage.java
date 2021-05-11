package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Controller.ControllerSwitch;

public class MoveToLeaderStorageMessage extends IntMessage{
    public MoveToLeaderStorageMessage(int integer) {
        super(integer);
    }

    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
