package it.polimi.ingsw.Messages.InGameMessages;

import it.polimi.ingsw.Controller.ControllerSwitch;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Messages.ResourceIntMessage;

public class ShelfSelectionMessage extends ResourceIntMessage {
    public ShelfSelectionMessage(int integer, Resource resource) {
        super(integer, resource);
    }

    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
