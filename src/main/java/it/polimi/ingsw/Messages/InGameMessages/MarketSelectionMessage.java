package it.polimi.ingsw.Messages.InGameMessages;

import it.polimi.ingsw.Controller.ControllerSwitch;
import it.polimi.ingsw.Messages.BooleanIntMessage;

public class MarketSelectionMessage extends BooleanIntMessage {
    public MarketSelectionMessage(int integer, boolean aBoolean) {
        super(integer, aBoolean);
    }

    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
