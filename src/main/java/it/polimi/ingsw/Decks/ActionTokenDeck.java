package it.polimi.ingsw.Decks;

import it.polimi.ingsw.Cards.ActionToken;
import it.polimi.ingsw.Enums.ActionTokenType;

import java.util.*;

public class ActionTokenDeck implements Deck{
    private List<ActionToken> active;
    private List<ActionToken> inactive;

    public ActionTokenDeck(){
        inactive = new ArrayList<>();
        active = new ArrayList<>();

        active.add(new ActionToken(ActionTokenType.DISCARDGREEN));
        active.add(new ActionToken(ActionTokenType.DISCARDBLUE));
        active.add(new ActionToken(ActionTokenType.DISCARDYELLOW));
        active.add(new ActionToken(ActionTokenType.DISCARDPURPLE));
        active.add(new ActionToken(ActionTokenType.RESETDECKONEFP));
        active.add(new ActionToken(ActionTokenType.TWOFP));
        active.add(new ActionToken(ActionTokenType.TWOFP));

        Collections.shuffle(active);
    }

    @Override
    public void shuffle() {
        Collections.shuffle(active);
    }

    public ActionToken draw() {
        ActionToken moving = active.remove(0);
        inactive.add(moving);
        return new ActionToken(moving.getType());
    }

    public void reset(){
        active.addAll(inactive);
        inactive.clear();
        this.shuffle();
    }
}
