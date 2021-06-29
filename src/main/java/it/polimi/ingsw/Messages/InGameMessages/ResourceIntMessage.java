package it.polimi.ingsw.Messages.InGameMessages;

import it.polimi.ingsw.Enums.Resource;

/**
 * The messages extending this class contains an integer (already from IntMessage) and a Resource
 */
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
