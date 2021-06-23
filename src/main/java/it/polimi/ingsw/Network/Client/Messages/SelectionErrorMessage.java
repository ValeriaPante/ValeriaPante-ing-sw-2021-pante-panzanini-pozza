package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class SelectionErrorMessage extends WithIntMessage{
    private String type = "selectionError";
    private final String error;

    public SelectionErrorMessage(String error, int cardId){
        this.id = cardId;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
