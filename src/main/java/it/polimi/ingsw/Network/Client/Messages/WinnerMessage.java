package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class WinnerMessage extends WithIntMessage{

    private String type =" winner";

    public WinnerMessage(int winnerId) {
        this.id = winnerId;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return true;
    }
}
