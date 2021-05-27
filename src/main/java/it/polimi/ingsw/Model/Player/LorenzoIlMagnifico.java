package it.polimi.ingsw.Model.Player;

import it.polimi.ingsw.Model.Decks.ActionTokenDeck;

public class LorenzoIlMagnifico extends Player{

    private ActionTokenDeck actionTokenDeck;

    public LorenzoIlMagnifico(){
        super("Lorenzo il Magnifico");
        this.actionTokenDeck = new ActionTokenDeck();
    }

    public ActionTokenDeck getActionTokenDeck() {
        return this.actionTokenDeck;
    }
}
