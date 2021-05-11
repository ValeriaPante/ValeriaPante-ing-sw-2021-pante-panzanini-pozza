package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Controller.ControllerSwitch;
import it.polimi.ingsw.Enums.Resource;

public class ShelfDeselectionMessage extends ResourceIntMessage{
    public ShelfDeselectionMessage(int integer, Resource resource) {
        super(integer, resource);
    }

    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
