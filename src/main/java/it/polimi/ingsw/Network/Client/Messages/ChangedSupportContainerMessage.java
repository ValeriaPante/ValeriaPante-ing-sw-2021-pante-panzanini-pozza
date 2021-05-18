package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Network.Client.Visitor;

import java.util.HashMap;

public class ChangedSupportContainerMessage extends WithIntMessage{

    private final HashMap<Resource, Integer> inside;

    public ChangedSupportContainerMessage(int playerId, HashMap<Resource, Integer> inside) {
        this.id = playerId;
        this.inside = inside;
    }

    public HashMap<Resource, Integer> getInside() {
        return inside;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
