package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Enums.ActionTokenType;
import it.polimi.ingsw.Network.Client.Visitor;

public class LorenzoTurnMessage extends FromServerMessage{

    private String type ="lorenzoTurn";
    private final ActionTokenType actionToken;

    public LorenzoTurnMessage(ActionTokenType actionToken) {
        this.actionToken = actionToken;
    }

    public ActionTokenType getActionToken() {
        return actionToken;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
