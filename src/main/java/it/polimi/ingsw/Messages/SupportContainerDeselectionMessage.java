package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Controller.ControllerSwitch;
import it.polimi.ingsw.Enums.Resource;

public class SupportContainerDeselectionMessage extends ResourceIntMessage{
    public SupportContainerDeselectionMessage(int integer, Resource resource) {
        super(integer, resource);
    }

    public void readThrough(ControllerSwitch controllerSwitch){
        controllerSwitch.actionOnMessage(this);
    }
}
