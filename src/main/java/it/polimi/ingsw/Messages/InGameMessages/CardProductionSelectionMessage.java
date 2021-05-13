package it.polimi.ingsw.Messages.InGameMessages;

import it.polimi.ingsw.Controller.ControllerSwitch;
import it.polimi.ingsw.Messages.IntMessage;

public class CardProductionSelectionMessage extends IntMessage {
    public CardProductionSelectionMessage(int integer) {
        super(integer);
    }

    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
