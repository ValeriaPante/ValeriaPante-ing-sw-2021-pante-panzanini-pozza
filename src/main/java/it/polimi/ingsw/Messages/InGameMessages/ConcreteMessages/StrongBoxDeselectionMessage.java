package it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages;

import it.polimi.ingsw.Controller.InGameControllerSwitch;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Messages.InGameMessages.ResourceIntMessage;

public class StrongBoxDeselectionMessage extends ResourceIntMessage {
    public StrongBoxDeselectionMessage(int integer, Resource resource) {
        super(integer, resource);
    }

    public void readThrough(InGameControllerSwitch inGameControllerSwitch){
        inGameControllerSwitch.actionOnMessage(this);
    }
}
