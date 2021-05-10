package it.polimi.ingsw.Messages;

import it.polimi.ingsw.Enums.Resource;

public class LeaderStorageSelectionMessage extends Message{
    private final int id;
    private final int resPosition;
    private final Resource resource;

    public LeaderStorageSelectionMessage(int id, int resPosition, Resource resource) {
        this.id = id;
        this.resPosition = resPosition;
        this.resource = resource;
    }

    public int getId() {
        return id;
    }

    public int getResPosition() {
        return resPosition;
    }

    public Resource getResource() {
        return resource;
    }
}