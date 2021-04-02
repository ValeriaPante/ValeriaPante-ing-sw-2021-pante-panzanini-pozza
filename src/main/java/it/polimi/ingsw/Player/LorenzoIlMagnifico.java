package it.polimi.ingsw.Player;

import it.polimi.ingsw.Cards.ActionToken;
import it.polimi.ingsw.Decks.ActionTokenDeck;

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
