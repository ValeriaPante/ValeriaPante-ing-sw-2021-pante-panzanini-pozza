package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public abstract class FromServerMessage {

    public boolean visit(Visitor v){ return false;}
}
