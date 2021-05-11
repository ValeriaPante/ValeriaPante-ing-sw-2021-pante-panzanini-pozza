package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Controller.ControllerSwitch;

public class QuitFromMarketMessage implements Message{
    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
