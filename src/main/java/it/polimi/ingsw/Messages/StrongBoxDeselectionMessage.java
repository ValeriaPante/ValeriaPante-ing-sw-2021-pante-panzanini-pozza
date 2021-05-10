package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Enums.Resource;

public class StrongBoxDeselectionMessage extends ResourceIntMessage{
    public StrongBoxDeselectionMessage(int integer, Resource resource) {
        super(integer, resource);
    }
}
