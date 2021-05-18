package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class StartMessage extends FromServerMessage{

    @Override
    public void visit(Visitor v){
        v.updateModel(this);
    }
}
