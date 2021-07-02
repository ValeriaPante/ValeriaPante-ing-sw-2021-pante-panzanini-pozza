package it.polimi.ingsw.Model.Decks;

import it.polimi.ingsw.Model.Cards.ActionToken;
import it.polimi.ingsw.Enums.ActionTokenType;

import java.util.*;

/**
 * Representation of a deck of Action Tokens
 */
public class ActionTokenDeck implements Deck{
    private final List<ActionToken> active;
    private final List<ActionToken> inactive;

    /**
     * Constructor
     */
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

    /**
     * Shuffles the deck
     */
    @Override
    public void shuffle() {
        Collections.shuffle(active);
    }

    /**
     * Draws from the deck
     * @return ActionToken on top of the deck
     */
    public ActionToken draw() {
        ActionToken moving = active.remove(0);
        inactive.add(moving);
        return new ActionToken(moving.getType());
    }

    /**
     * Resets the deck so it has all the tokens
     */
    public void reset(){
        active.addAll(inactive);
        inactive.clear();
        this.shuffle();
    }
}
