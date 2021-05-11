package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Controller.ControllerSwitch;

public class CardProductionSelectionMessage extends IntMessage{
    public CardProductionSelectionMessage(int integer) {
        super(integer);
    }

    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
