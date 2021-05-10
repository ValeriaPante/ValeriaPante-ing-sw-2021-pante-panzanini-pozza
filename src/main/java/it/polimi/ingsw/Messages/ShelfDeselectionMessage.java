package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Enums.Resource;

public class ShelfDeselectionMessage extends ResourceIntMessage{
    public ShelfDeselectionMessage(int integer, Resource resource) {
        super(integer, resource);
    }
}
