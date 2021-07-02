package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Enums.ActionTokenType;

/**
 * Action Token representation
 */
public class ActionToken {
    private final ActionTokenType type;

    /**
     * Constructor
     * @param type the token type
     */
    public ActionToken(ActionTokenType type){
        this.type = type;
    }

    /**
     * Getter
     * @return this token type
     */
    public ActionTokenType getType(){
        return this.type;
    }
}
