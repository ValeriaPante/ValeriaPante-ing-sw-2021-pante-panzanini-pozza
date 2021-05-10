package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Enums.Resource;

public class StrongBoxSelectionMessage extends ResourceIntMessage{
    public StrongBoxSelectionMessage(int integer, Resource resource) {
        super(integer, resource);
    }
}
