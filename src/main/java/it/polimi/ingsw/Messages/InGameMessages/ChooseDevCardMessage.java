package it.polimi.ingsw.Messages.InGameMessages;

import it.polimi.ingsw.Controller.ControllerSwitch;
import it.polimi.ingsw.Messages.IntMessage;

public class ChooseDevCardMessage extends IntMessage {
    public ChooseDevCardMessage(int integer) {
        super(integer);
    }

    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
