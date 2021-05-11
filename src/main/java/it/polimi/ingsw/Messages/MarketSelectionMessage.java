package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Controller.ControllerSwitch;

public class MarketSelectionMessage extends BooleanIntMessage{
    public MarketSelectionMessage(int integer, boolean aBoolean) {
        super(integer, aBoolean);
    }

    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
