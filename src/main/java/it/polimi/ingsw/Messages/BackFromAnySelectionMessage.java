package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Controller.ControllerSwitch;

public class BackFromAnySelectionMessage implements Message{
    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
