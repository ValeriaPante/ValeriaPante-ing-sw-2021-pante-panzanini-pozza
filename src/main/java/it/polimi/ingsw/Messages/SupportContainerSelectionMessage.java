package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Enums.Resource;

public class SupportContainerSelectionMessage extends ResourceIntMessage{
    public SupportContainerSelectionMessage(int integer, Resource resource) {
        super(integer, resource);
    }
}
