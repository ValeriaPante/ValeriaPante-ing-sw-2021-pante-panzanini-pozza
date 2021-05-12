package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class ErrorMessage extends WithIntMessage{

    private final String error;

    public ErrorMessage(int playerId, String error) {
        this.id = playerId;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void visit(Visitor v){ v.updateModel(this); }
}
