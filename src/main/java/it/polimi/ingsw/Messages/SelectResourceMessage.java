package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Enums.Resource;

public class SelectResourceMessage extends ResourceIntMessage{
    public SelectResourceMessage(int integer, Resource resource) {
        super(integer, resource);
    }
}
