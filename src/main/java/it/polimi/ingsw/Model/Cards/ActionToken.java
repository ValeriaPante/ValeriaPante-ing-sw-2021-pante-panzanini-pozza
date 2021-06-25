package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Enums.ActionTokenType;

/**
 * Action Token representation
 */
public class ActionToken {
    private final ActionTokenType type;

    public ActionToken(ActionTokenType type){
        this.type = type;
    }

    public ActionTokenType getType(){
        return this.type;
    }
}
