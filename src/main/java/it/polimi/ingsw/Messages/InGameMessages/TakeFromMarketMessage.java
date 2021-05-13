package it.polimi.ingsw.Messages.InGameMessages;

import it.polimi.ingsw.Controller.ControllerSwitch;
import it.polimi.ingsw.Messages.Message;

public class TakeFromMarketMessage implements Message {
    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
