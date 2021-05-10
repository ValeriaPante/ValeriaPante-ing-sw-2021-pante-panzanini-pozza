package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Enums.Resource;

public class ShelfSelectionMessage extends ResourceIntMessage{
    public ShelfSelectionMessage(int integer, Resource resource) {
        super(integer, resource);
    }
}
