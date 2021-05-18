package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Network.Client.Visitor;

public class ChangedShelfMessage extends WithIntMessage{

    private final int numberOfShelf;
    private final Resource resourceType;
    private final int quantity;

    public ChangedShelfMessage(int playerId, int numberOfShelf, Resource resourceType, int quantity) {
        this.id = playerId;
        this.numberOfShelf = numberOfShelf;
        this.resourceType = resourceType;
        this.quantity = quantity;
    }

    public int getNumberOfShelf() {
        return numberOfShelf;
    }

    public Resource getResourceType() {
        return resourceType;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
