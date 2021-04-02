package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Enums.ActionTokenType;

public class ActionToken {
    private final ActionTokenType type;

    public ActionToken(ActionTokenType type){
        this.type = type;
    }

    public ActionTokenType getType(){
        return this.type;
    }
}
