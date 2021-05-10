package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Enums.Resource;

public class AnySelectionMessage extends Message{
    private final Resource resource;

    public AnySelectionMessage(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}
