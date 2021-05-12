package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class BroadcastErrorMessage extends FromServerMessage{

    private final String error;

    public BroadcastErrorMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void visit(Visitor v){ v.updateModel(this); }
}
