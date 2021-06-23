package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Network.Client.Visitor;

public class ChangedLeaderStorageMessage extends WithIntMessage{

    private String type = "changedLeaderStorage";
    private final int cardId;
    private final Resource[] owned;

    public ChangedLeaderStorageMessage(int playerId, int cardId, Resource[] owned) {
        this.id = playerId;
        this.cardId = cardId;
        this.owned = owned;
    }

    public int getCardId() {
        return cardId;
    }

    public Resource[] getOwned() {
        return owned;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
