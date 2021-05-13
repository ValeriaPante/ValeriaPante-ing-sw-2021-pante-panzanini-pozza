package it.polimi.ingsw.Messages.InGameMessages;

import it.polimi.ingsw.Enums.Resource;

public abstract class ResourceIntMessage extends IntMessage{
    private final Resource resource;

    public ResourceIntMessage(int integer, Resource resource) {
        super(integer);
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}
